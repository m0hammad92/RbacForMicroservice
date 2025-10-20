package com.lidaa.accounts.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "identity")
public class AuthFlowConfig {
    private boolean clientCredentialsFlow;
    private String clientId;
    private String clientSecret;
    private String grantType;
    private String scope;
    private String tokenUrl;


}
