package com.jeongyuneo.springsecurity.member.entity;

import com.jeongyuneo.springsecurity.member.dto.SignupRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    public Member(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public static Member of(SignupRequest signupRequest, PasswordEncoder passwordEncoder) {
        return new Member(signupRequest.username(), passwordEncoder.encode(signupRequest.password()), Role.USER);
    }
}
