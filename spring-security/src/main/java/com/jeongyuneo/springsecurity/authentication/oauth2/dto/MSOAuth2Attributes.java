package com.jeongyuneo.springsecurity.authentication.oauth2.dto;

import java.util.Map;

public class MSOAuth2Attributes extends OAuth2Attributes {

    private static final String ID = "sub";
    private static final String NAME = "name";
    private static final String EMAIL = "email";

    public MSOAuth2Attributes(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return String.valueOf(attributes.get(ID));
    }

    @Override
    public String getName() {
        return String.valueOf(attributes.get(NAME));
    }

    @Override
    public String getEmail() {
        return String.valueOf(attributes.get(EMAIL));
    }
}
