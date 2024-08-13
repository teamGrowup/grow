package org.boot.growup.common.oauth2.naver.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverAccountResponseDTO {
    private String resultcode;
    private String message;
    private Response response;

    @Data
    public static class Response {
        private String email;

        private String nickname;

        @JsonProperty("profile_image")
        private String profileImage;

        private String gender;

        private String id;

        private String name;

        private String birthday;
    }
}
