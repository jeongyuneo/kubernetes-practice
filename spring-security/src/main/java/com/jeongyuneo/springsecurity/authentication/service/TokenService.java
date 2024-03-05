package com.jeongyuneo.springsecurity.authentication.service;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

public interface TokenService {

    String issueAccessToken(Long id);

    Optional<String> extractAccessToken(HttpServletRequest request);

    Long extractMemberId(String accessToken);
}
