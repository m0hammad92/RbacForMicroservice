package com.lidaa.accounts.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix= "jwt-mapping")
public class JwtMappingProperties {

    private String authorityPrefix;

    private String principalClaim;

    private String identityProvider;

}
