package com.jeongyuneo.springsecurity.authentication.oauth2.dto;

import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.entity.SocialType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2UserInfo {

    private final SocialType socialType;
    private final String nameAttributeKey;
    private final OAuth2Attributes oauth2Attributes;

    public static OAuth2UserInfo of(SocialType socialType, String nameAttributeKey, OAuth2Attributes oAuth2Attributes) {
        return new OAuth2UserInfo(socialType, nameAttributeKey, oAuth2Attributes);
    }

    public Member toMember() {
        return Member.ofSocial(oauth2Attributes.getId(), socialType, oauth2Attributes.getName(), oauth2Attributes.getEmail());
    }

    public String getSocialId() {
        return oauth2Attributes.getId();
    }
}
