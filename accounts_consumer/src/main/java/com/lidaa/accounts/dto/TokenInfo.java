package com.lidaa.accounts.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class TokenInfo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1l;


    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expires;

    @JsonProperty("refresh_expires_in")
    private String refreshExpires;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("id_token")
    private String idToken;

    @JsonProperty("not-before-policy")
    private String notBeforePolicy;

    @JsonProperty("scope")
    private String scope;

    @JsonProperty("token")
    private String token;


}
