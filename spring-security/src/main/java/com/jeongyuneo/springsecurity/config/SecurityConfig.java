package com.jeongyuneo.springsecurity.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeongyuneo.springsecurity.authentication.filter.CustomJsonUsernamePasswordAuthenticationFilter;
import com.jeongyuneo.springsecurity.authentication.filter.ExceptionHandlingFilter;
import com.jeongyuneo.springsecurity.authentication.login.handler.LoginFailureHandler;
import com.jeongyuneo.springsecurity.authentication.login.handler.LoginSuccessHandler;
import com.jeongyuneo.springsecurity.authentication.login.service.LoginService;
import com.jeongyuneo.springsecurity.authentication.token.handler.JwtAuthenticationEntryPoint;
import com.jeongyuneo.springsecurity.authentication.token.filter.JwtAuthenticationProcessingFilter;
import com.jeongyuneo.springsecurity.authentication.token.service.TokenService;
import com.jeongyuneo.springsecurity.member.service.MemberReadService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private static final String[] AUTH_EXCLUDING_REQUESTS = {"/members", "/login"};

    private final ObjectMapper objectMapper;
    private final TokenService tokenService;
    private final LoginService loginService;
    private final MemberReadService memberReadService;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(AUTH_EXCLUDING_REQUESTS).permitAll()
                        .anyRequest().authenticated())
                .exceptionHandling(exceptionHandler -> exceptionHandler.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterAfter(customAuthenticationProcessingFilter(), LogoutFilter.class)
                .addFilterBefore(jwtAuthenticationProcessingFilter(), CustomJsonUsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationProcessingFilter.class)
                .build();
    }

    @Bean
    public CustomJsonUsernamePasswordAuthenticationFilter customAuthenticationProcessingFilter() {
        CustomJsonUsernamePasswordAuthenticationFilter customJsonUsernamePasswordAuthenticationFilter
                = new CustomJsonUsernamePasswordAuthenticationFilter(objectMapper);
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationManager(authenticationManager());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        customJsonUsernamePasswordAuthenticationFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return customJsonUsernamePasswordAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(loginService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(tokenService, memberReadService);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(objectMapper, tokenService, memberReadService);
    }

    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(objectMapper);
    }

    @Bean
    public ExceptionHandlingFilter exceptionHandlerFilter() {
        return new ExceptionHandlingFilter(objectMapper);
    }
}
