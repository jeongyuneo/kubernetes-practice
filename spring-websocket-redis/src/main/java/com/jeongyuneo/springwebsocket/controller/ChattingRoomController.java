package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomCreateRequest;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chattings/rooms")
public class ChattingRoomController {

    private final ChattingService chattingService;

    @PostMapping
    public void createChattingRoom(@RequestBody ChattingRoomCreateRequest chattingRoomCreateRequest) {
        chattingService.createChattingRoom(chattingRoomCreateRequest);
    }
}
