package com.jeongyuneo.springwebsocket.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.UUID;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoom implements Serializable {

    private static final long SERIAL_VERSION_UID = 6494678977089006639L;

    @Id
    private final UUID id;

    private final String name;

    public static ChattingRoom from(String name) {
        return new ChattingRoom(UUID.randomUUID(), name);
    }
}
