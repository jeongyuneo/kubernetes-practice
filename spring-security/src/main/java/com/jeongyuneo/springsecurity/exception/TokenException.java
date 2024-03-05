package com.jeongyuneo.springsecurity.exception;

public class TokenException extends ApplicationException {

    public TokenException(ApplicationExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
}
