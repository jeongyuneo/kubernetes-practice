package com.jeongyuneo.springsecurity.exception;

public class NotFoundException extends ApplicationException {

    public NotFoundException(ApplicationExceptionInfo exceptionInfo) {
        super(exceptionInfo);
    }
}
