package com.jeongyuneo.springsecurity.authentication.token.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.util.Date;

@Getter
@RequiredArgsConstructor
public enum Jwt {

    ACCESS_TOKEN("Authorization", Duration.ofMinutes(10)),
    REFRESH_TOKEN("Authorization-Refresh", Duration.ofMinutes(30));

    private final String headerName;
    private final Duration expirationTime;

    public Date getExpirationTimeDate() {
        return new Date(System.currentTimeMillis() + this.expirationTime.toMillis());
    }
}
