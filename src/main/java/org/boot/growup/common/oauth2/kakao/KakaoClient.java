package org.boot.growup.common.oauth2.kakao;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class KakaoClient {
    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.kakao.grant-type}")
    private String grantType;
}
