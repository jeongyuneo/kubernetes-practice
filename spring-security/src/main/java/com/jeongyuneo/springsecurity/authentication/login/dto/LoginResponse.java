package com.jeongyuneo.springsecurity.authentication.login.dto;

public record LoginResponse(
        String accessToken
) {

    public static LoginResponse from(String accessToken) {
        return new LoginResponse(accessToken);
    }
}
