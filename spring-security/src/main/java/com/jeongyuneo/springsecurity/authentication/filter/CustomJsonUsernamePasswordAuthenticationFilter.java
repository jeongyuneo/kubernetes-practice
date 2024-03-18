package com.jeongyuneo.springsecurity.authentication.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongyuneo.springsecurity.exception.ApplicationExceptionInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login/regular";
    private static final String USERNAME_KEY = "username";
    private static final String PASSWORD_KEY = "password";

    private final ObjectMapper objectMapper;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL));
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        validateContentType(request.getContentType());
        Map<String, String> usernamePasswordMap = objectMapper.readValue(StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8), Map.class);
        String username = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);
        return getAuthenticationManager().authenticate(UsernamePasswordAuthenticationToken.unauthenticated(username, password));
    }

    private void validateContentType(String contentType) {
        if (contentType == null || !contentType.equals(MediaType.APPLICATION_JSON_VALUE)) {
            throw new AuthenticationServiceException(ApplicationExceptionInfo.UNSUPPORTED_CONTENT_TYPE.getMessage());
        }
    }
}
