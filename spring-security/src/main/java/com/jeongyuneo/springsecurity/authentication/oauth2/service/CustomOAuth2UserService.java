package com.jeongyuneo.springsecurity.authentication.oauth2.service;

import com.jeongyuneo.springsecurity.authentication.oauth2.dto.OAuth2UserInfo;
import com.jeongyuneo.springsecurity.authentication.oauth2.entity.CustomOAuth2User;
import com.jeongyuneo.springsecurity.member.entity.Member;
import com.jeongyuneo.springsecurity.member.entity.SocialType;
import com.jeongyuneo.springsecurity.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        SocialType socialType = SocialType.find(userRequest.getClientRegistration().getRegistrationId());
        String userNameAttributeName = getUserNameAttributeName(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();
        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfo.of(socialType, userNameAttributeName, socialType.toOAuthAttributes(attributes));
        Member member = getMemberFromOAuth2UserInfo(oAuth2UserInfo);
        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().name())),
                attributes,
                userNameAttributeName,
                member.getUsername(),
                member.getRole()
        );
    }

    private String getUserNameAttributeName(OAuth2UserRequest userRequest) {
        return userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
    }

    private Member getMemberFromOAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo) {
        return memberRepository.findBySocialIdAndSocialType(oAuth2UserInfo.getSocialId(), oAuth2UserInfo.getSocialType())
                .orElseGet(() -> memberRepository.save(oAuth2UserInfo.toMember()));
    }
}
