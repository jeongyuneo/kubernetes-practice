package com.jeongyuneo.springsecurity.authentication.token.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.jeongyuneo.springsecurity.authentication.token.entity.Jwt;
import com.jeongyuneo.springsecurity.exception.ApplicationExceptionInfo;
import com.jeongyuneo.springsecurity.exception.TokenException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class JwtService implements TokenService {

    private static final String BEARER = "Bearer ";
    private static final String REMOVE = "";
    private static final String MEMBER_ID_CLAIM = "MemberId";

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Override
    public String issueAccessToken(Long id) {
        return JWT.create()
                .withSubject(Jwt.ACCESS_TOKEN.name())
                .withExpiresAt(Jwt.ACCESS_TOKEN.getExpirationTimeDate())
                .withClaim(MEMBER_ID_CLAIM, id)
                .sign(Algorithm.HMAC512(secretKey));
    }

    @Override
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(Jwt.ACCESS_TOKEN.getHeaderName()))
                .map(this::removePrefix);
    }

    @Override
    public Long extractMemberId(String accessToken) {
        return JWT.decode(accessToken)
                .getClaim(MEMBER_ID_CLAIM)
                .asLong();
    }

    private String removePrefix(String accessToken) {
        validateTokenType(accessToken);
        return accessToken.replace(BEARER, REMOVE);
    }

    private void validateTokenType(String token) {
        if (!token.startsWith(BEARER)) {
            throw new TokenException(ApplicationExceptionInfo.INVALID_TOKEN_TYPE);
        }
    }
}
