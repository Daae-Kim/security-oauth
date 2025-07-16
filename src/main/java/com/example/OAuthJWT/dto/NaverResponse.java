package com.example.OAuthJWT.dto;

import java.io.Serializable;

public class NaverResponse implements OAuth2Response{

    @Override
    public String getProvider() {
        return "";
    }

    @Override
    public String getProviderId() {
        return "";
    }

    @Override
    public String getEmail() {
        return "";
    }

    @Override
    public String getName() {
        return "";
    }

}
