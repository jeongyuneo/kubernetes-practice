package com.jeongyuneo.springsecurity.member.dto;

public record SignupResponse(
        Long id
) {

    public static SignupResponse from(Long id) {
        return new SignupResponse(id);
    }
}
