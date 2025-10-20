package com.lidaa.accounts.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonConstants {
    BEARER("Bearer"),
    CLIENT_ID("client_id"),
    CLIENT_SECRET("client_secret"),
    GRANT_TYPE("grant_type"),
    SCOPE("scope"),
    USERNAME("username"),
    PASSWORD("password")
    ;



    private final String value;


}
