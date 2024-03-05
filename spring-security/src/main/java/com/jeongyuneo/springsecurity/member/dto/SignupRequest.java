package com.jeongyuneo.springsecurity.member.dto;

public record SignupRequest(
        String username,
        String password
) {
}
