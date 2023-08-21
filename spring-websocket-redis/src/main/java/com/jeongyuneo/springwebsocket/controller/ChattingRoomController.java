package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomCreateRequest;
import com.jeongyuneo.springwebsocket.dto.Message;
import com.jeongyuneo.springwebsocket.entity.ChattingRoom;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public List<ChattingRoom> getChattingRooms() {
        return chattingService.getChattingRooms();
    }

    @GetMapping("/detail/{chattingRoomId}")
    public ChattingRoom getChattingRoom(@PathVariable String chattingRoomId) {
        return chattingService.getChattingRoom(chattingRoomId);
    }

    @GetMapping("/{chattingRoomId}")
    public List<Message> getChattings(@PathVariable String chattingRoomId) {
        return chattingService.getChattings(chattingRoomId);
    }
}
