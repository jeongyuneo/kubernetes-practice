package com.jeongyuneo.springsecurity.member.controller;

import com.jeongyuneo.springsecurity.member.dto.SignupRequest;
import com.jeongyuneo.springsecurity.member.dto.SignupResponse;
import com.jeongyuneo.springsecurity.member.service.MemberWriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberWriteService memberWriteService;

    @PostMapping
    public ResponseEntity<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        return ResponseEntity.ok().body(memberWriteService.signup(signupRequest));
    }
}
