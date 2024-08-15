package org.boot.growup.common.oauth2.naver;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class NaverClient {
    @Value("${oauth2.naver.client-id}")
    private String clientId;

    @Value("${oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth2.naver.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.naver.grant-type}")
    private String grantType;
}
