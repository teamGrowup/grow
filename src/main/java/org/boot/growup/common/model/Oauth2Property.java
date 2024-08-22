package org.boot.growup.common.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "oauth2")
public class Oauth2Property {

    private Google google;
    private Kakao kakao;
    private Naver naver;

    @Data
    public static class Google {
        private Client client;
        private String accessScope;
        private String callbackUri;
        private String grantType;

        @Data
        public static class Client {
            private String id;
            private String secret;
        }
    }

    @Data
    public static class Kakao {
        private String clientId;
        private String redirectUri;
        private String grantType;
    }

    @Data
    public static class Naver {
        private String clientId;
        private String clientSecret;
        private String redirectUri;
        private String grantType;
    }
}
