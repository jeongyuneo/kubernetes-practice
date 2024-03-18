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
    private String name;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId;

    public Member(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public Member(String socialId, SocialType socialType, Role role, String name, String email) {
        this.username = email;
        this.name = name;
        this.email = email;
        this.socialId = socialId;
        this.socialType = socialType;
        this.role = role;
    }

    public static Member of(SignupRequest signupRequest, PasswordEncoder passwordEncoder) {
        return new Member(signupRequest.username(), passwordEncoder.encode(signupRequest.password()), Role.USER);
    }

    public static Member ofSocial(String socialId, SocialType socialType, String name, String email) {
        return new Member(socialId, socialType, Role.USER, name, email);
    }
}
