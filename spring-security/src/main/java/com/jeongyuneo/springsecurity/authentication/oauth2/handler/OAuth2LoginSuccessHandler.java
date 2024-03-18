package com.jeongyuneo.springsecurity.authentication.oauth2.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongyuneo.springsecurity.authentication.login.dto.LoginResponse;
import com.jeongyuneo.springsecurity.authentication.oauth2.entity.CustomOAuth2User;
import com.jeongyuneo.springsecurity.authentication.token.service.TokenService;
import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.service.MemberReadService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
@Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final MemberReadService memberReadService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException {
        String username = extractUsername(authentication);
        Member member = memberReadService.getMemberByUsername(username);
        setResponseBody(response, tokenService.issueAccessToken(member.getId()));
    }

    private String extractUsername(Authentication authentication) {
        CustomOAuth2User userDetails = (CustomOAuth2User) authentication.getPrincipal();
        return userDetails.getUsername();
    }

    private void setResponseBody(HttpServletResponse response, String accessToken) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.getWriter().write(objectMapper.writeValueAsString(LoginResponse.from(accessToken)));
    }
}
