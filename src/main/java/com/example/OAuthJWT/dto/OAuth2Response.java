package com.example.OAuthJWT.dto;

public interface OAuth2Response {

    String getProvider();
    String getProviderId();
    String getEmail();
    String getName();
    default String getBirthYear(){
        return null;
    };
}
