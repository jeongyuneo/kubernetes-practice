package com.jeongyuneo.springsecurity.exception.dto;

public record ExceptionResponse(
        String message
) {

    public static ExceptionResponse from(String message) {
        return new ExceptionResponse(message);
    }
}
