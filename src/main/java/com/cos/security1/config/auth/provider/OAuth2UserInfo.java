package com.cos.security1.config.auth.provider;

public interface OAuth2UserInfo {
    String getProviderID(); // id
    String getProvider(); // facebook or google
    String getEmail(); //email
    String getName(); //name
}
