package org.boot.growup.auth.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
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
