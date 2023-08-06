package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomCreateRequest;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/chattings/rooms")
public class ChattingRoomController {

    private final ChattingService chattingService;

    @PostMapping
    public void createChattingRoom(@RequestBody ChattingRoomCreateRequest chattingRoomCreateRequest) {
        chattingService.createChattingRoom(chattingRoomCreateRequest);
    }

    @PostMapping("{chattingRoomId}")
    public void enterChattingRoom(@PathVariable String chattingRoomId) {
        chattingService.enterChattingRoom(chattingRoomId);
    }
}
