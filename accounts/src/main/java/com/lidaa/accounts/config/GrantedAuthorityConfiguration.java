package com.lidaa.accounts.config;


import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Configuration
@RequiredArgsConstructor
public class GrantedAuthorityConfiguration {

    private final JwtMappingProperties mappingProperties;

    @ConditionalOnProperty(prefix = "jwt-mapping", name="identityProvider",havingValue = "keycloak")
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> keyJwtCollectionConverter(){
        return new KeycloakGrantedAuthorityConverter();
    }

    @ConditionalOnMissingBean
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtCollectionConverter(){
        JwtGrantedAuthoritiesConverter converter = new JwtGrantedAuthoritiesConverter();
        if(StringUtils.hasText(mappingProperties.getAuthorityPrefix())){
            converter.setAuthorityPrefix(mappingProperties.getAuthorityPrefix().trim());
        }
        if(StringUtils.hasText(mappingProperties.getPrincipalClaim())){
            converter.setAuthoritiesClaimName(mappingProperties.getPrincipalClaim().trim());
        }
        return  converter;
    }

}
