package org.boot.growup.common.oauth2.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE;

@Component
@Slf4j
@RequiredArgsConstructor
public class GoogleOauthService {
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

    private final GoogleAuthFeignClient googleAuthFeignClient;
    private final GoogleTokenFeignClient googleTokenFeignClient;
    private final GoogleUserInfoFeignClient googleUserInfoFeignClient;
    public String sendAuthorizationRequest() {
        String body = "client_id=" + googleClientId
                    + "&redirect_uri=" + googleCallbackUri
                    + "&response_type=code"
                    + "&scope=" + googleDataAccessScope;

        return googleAuthFeignClient.sendAuthorizationRequest(
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                body
        );
    }

    public String requestGoogleAccessToken(String authCode) {
        String body = "code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8)
                    + "&client_id=" + googleClientId
                    + "&client_secret=" + googleClientSecret
                    + "&redirect_uri=" + googleCallbackUri
                    + "&grant_type=" + grantType;

        GoogleAccessTokenResponseDTO response = googleTokenFeignClient.requestAccessToken(
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                body
        );

        log.info("Google AccessToken : {}", response);

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE))
                .accessToken();
    }

    public GoogleAccountResponseDTO requestGoogleAccount(String accessToken) {
        return googleUserInfoFeignClient.requestGoogleAccount("Bearer " + accessToken);
    }
}
