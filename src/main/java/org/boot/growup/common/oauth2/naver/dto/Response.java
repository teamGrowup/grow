package org.boot.growup.common.oauth2.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Response(
    String email,
    String nickname,
    @JsonProperty("profile_image")
    String profileImage,
    String gender,
    String id,
    String name,
    String birthday
) {}
