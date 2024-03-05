package com.jeongyuneo.springsecurity.authentication.token.filter;

import com.jeongyuneo.springsecurity.authentication.token.service.TokenService;
import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.service.MemberReadService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final MemberReadService memberReadService;
    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        tokenService.extractAccessToken(request)
                .ifPresent(accessToken -> {
                    Member member = memberReadService.getMemberById(tokenService.extractMemberId(accessToken));
                    saveAuthentication(member);
                });
        filterChain.doFilter(request, response);
    }

    private void saveAuthentication(Member member) {
        UserDetails userDetails = toUserDetails(member);
        Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(
                userDetails, null, authoritiesMapper.mapAuthorities(userDetails.getAuthorities()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails toUserDetails(Member member) {
        return User.builder()
                .username(member.getUsername())
                .password(member.getPassword())
                .build();
    }
}
