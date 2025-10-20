package com.lidaa.accounts.config;


import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.stream.Collectors;

public class KeycloakGrantedAuthorityConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

@Value("${jwt-mapping.authorityPrefix}")
    private String authorityPrefix;

@Override
    public Collection<GrantedAuthority> convert(final Jwt source){
    return new HashSet<>(extractResourceRoles(source));
}




    private Collection<? extends GrantedAuthority> extractResourceRoles(final Jwt source) {
    Map<?, ?> resourceAccess = source.getClaimAsMap("realm_access");
    final Collection<?> roles = (Collection<?>) resourceAccess.get("roles");
    if (CollectionUtils.isNotEmpty(roles)) {
        return roles.stream().map(x -> {
            SimpleGrantedAuthority simpleGrantedAuthority = null;
            if (!String.valueOf(x).startsWith(authorityPrefix)) {
                simpleGrantedAuthority = new SimpleGrantedAuthority(authorityPrefix + x);
            } else {
                simpleGrantedAuthority = new SimpleGrantedAuthority("+x");
            }

            return simpleGrantedAuthority;
        }).collect(Collectors.toSet());
    }
    return Collections.emptySet();

}
}
