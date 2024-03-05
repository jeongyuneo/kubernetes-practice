package com.jeongyuneo.springsecurity.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {

    protected final HttpStatus status;
    protected final String message;

    public ApplicationException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public ApplicationException(ApplicationExceptionInfo exceptionInfo) {
        this(exceptionInfo.getStatus(), exceptionInfo.getMessage());
    }
}
