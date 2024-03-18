package com.jeongyuneo.springsecurity.member.entity;

import com.jeongyuneo.springsecurity.authentication.oauth2.dto.MSOAuth2Attributes;
import com.jeongyuneo.springsecurity.authentication.oauth2.dto.OAuth2Attributes;
import com.jeongyuneo.springsecurity.exception.ApplicationExceptionInfo;
import com.jeongyuneo.springsecurity.exception.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public enum SocialType {

    MS("azure") {
        @Override
        public OAuth2Attributes toOAuthAttributes(Map<String, Object> attributes) {
            return new MSOAuth2Attributes(attributes);
        }
    };

    private final String registrationId;

    public static SocialType find(String registrationId) {
        return Arrays.stream(values())
                .filter(value -> value.registrationId.equals(registrationId))
                .findAny()
                .orElseThrow(() -> new NotFoundException(ApplicationExceptionInfo.UNSUPPORTED_SOCIAL_LOGIN));
    }

    public abstract OAuth2Attributes toOAuthAttributes(Map<String, Object> attributes);
}
