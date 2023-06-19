package com.jeongyuneo.springwebsocket.controller;

import com.jeongyuneo.springwebsocket.dto.ChattingRoomResponse;
import com.jeongyuneo.springwebsocket.service.ChattingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChattingRoomController {

    private final ChattingService chattingService;

    @GetMapping("/chattings/members/{memberId}")
    public ResponseEntity<List<ChattingRoomResponse>> getAll(@PathVariable Long memberId) {
        return ResponseEntity.ok().body(chattingService.getAll(memberId));
    }
}
