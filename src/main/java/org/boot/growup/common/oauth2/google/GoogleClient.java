package org.boot.growup.common.oauth2.google;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class GoogleClient {
    @Value("${oauth2.google.client.id}")
    private String googleClientId;

    @Value("${oauth2.google.client.secret}")
    private String googleClientSecret;

    @Value("${oauth2.google.access-scope}")
    private String googleDataAccessScope;

    @Value("${oauth2.google.callback-uri}")
    private String googleCallbackUri;

    @Value("${oauth2.google.grant-type}")
    private String grantType;
}
