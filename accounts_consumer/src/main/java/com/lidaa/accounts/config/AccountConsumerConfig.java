package com.lidaa.accounts.config;

import brave.Tracing;
import brave.baggage.BaggageFields;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lidaa.accounts.client.AccountsClient;
import com.lidaa.accounts.constants.CommonConstants;
import com.lidaa.accounts.dto.TokenInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountConsumerConfig {

    private final HttpServletRequest httpServletRequest;
    private final AuthFlowConfig authFlowConfig;
    private final Tracing tracing;


    @Value("$(logging.level.com.lidaa.accounts)")
    private String loggingLevel;


    @Value("$(microservice.accountBaseUri)")
    private String accountBaseUri;

    @Bean
    AccountsClient getAccountClient() {
        return buildClient(this.accountBaseUri, AccountsClient.class, Optional.empty());
    }


    private <T> T buildClient(final String baseUri, final Class<T> clazz, Optional<WebClient.Builder> optWebClientBuilder) {
        HttpClient httpClient = HttpClient.create().responseTimeout(Duration.ofSeconds(120)).protocol(HttpProtocol.HTTP11, HttpProtocol.H2C);
        WebClient.Builder builder = optWebClientBuilder.orElseGet(WebClient::builder);
        WebClient webClient = builder.clientConnector(new ReactorClientHttpConnector(httpClient)).baseUrl(baseUri).filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> injectHeader(clientRequest))).build();
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(WebClientAdapter.create(webClient)).build();
        return factory.createClient(clazz);
    }

    private Mono<ClientRequest> injectHeader(final ClientRequest clientRequest) {
        String token = getBearerToken();
        String tokenPrefix = "Bearer";

        ClientRequest.Builder builder = ClientRequest.from(clientRequest).header(BaggageFields.TRACE_ID.name(), MDC.get(BaggageFields.TRACE_ID.name()));
        return Mono.just(builder
                .header(HttpHeaders.AUTHORIZATION, tokenPrefix + " " + token)
                .header(CommonConstants.USERNAME.getValue(), SecurityContextHolder.getContext().getAuthentication().getName())
                .build());


    }

    private String getBearerToken() {


        MultiValueMap<String, String> bodyValues = new LinkedMultiValueMap<>();
        bodyValues.add(CommonConstants.CLIENT_ID.getValue(), authFlowConfig.getClientId());
        bodyValues.add(CommonConstants.CLIENT_SECRET.getValue(), authFlowConfig.getClientSecret());
        bodyValues.add(CommonConstants.GRANT_TYPE.getValue(), authFlowConfig.getGrantType());
        bodyValues.add(CommonConstants.SCOPE.getValue(), authFlowConfig.getScope());

        String response = WebClient.create().post().uri(authFlowConfig.getTokenUrl()).contentType(MediaType.APPLICATION_FORM_URLENCODED).body(BodyInserters.fromFormData(bodyValues)).retrieve().bodyToMono(String.class).block();
        try {
            TokenInfo tokenInfo = new ObjectMapper().readValue(response, TokenInfo.class);
            if (Objects.nonNull(tokenInfo)) {
                String returnValue = tokenInfo.getAccessToken();
                log.trace("Bearer Token {}", returnValue);
                return returnValue;
            } else {
                log.error("Error Occurred while fetching token");
            }

        } catch (JsonProcessingException e) {
            log.error("Error Occurred while calling downstream {} ", e.getMessage());
        }
        return "";

    }


}
