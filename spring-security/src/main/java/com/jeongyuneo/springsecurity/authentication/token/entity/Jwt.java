package com.jeongyuneo.springsecurity.authentication.token.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public enum Jwt {

    ACCESS_TOKEN("Authorization", Duration.ofMinutes(10));

    private final String headerName;
    private final Duration expirationTime;

    public Date getExpirationTimeDate() {
        return new Date(System.currentTimeMillis() + this.expirationTime.toMillis());
    }
}
