package com.mysite.sbb.Config.OAuth2;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo {

    private String id;
    private Map<String, Object> kakaoAccount;

    public KakaoUserInfo(Map<String, Object> attributes, String id) {
        this.kakaoAccount = attributes;
        this.id = id;
    }

    @Override
    public String getProviderId() {
        return id;
    }

    @Override
    public String getProvider() {
        return "KAKAO";
    }

    @Override
    public String getEmail() {
        return String.valueOf(kakaoAccount.get("email"));
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getImage()
    {
        return String.valueOf(kakaoAccount.get("picture"));
    }
}
