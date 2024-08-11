package org.boot.growup.common.oauth2.kakao;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.oauth2.google.dto.GoogleAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.kakao.call.KakaoTokenFeignClient;
import org.boot.growup.common.oauth2.kakao.call.KakaoUserInfoFeignClient;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class KakaoOauthService {
    @Value("${oauth2.kakao.client-id}")
    private String clientId;

    @Value("${oauth2.kakao.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.kakao.grant-type}")
    private String grantType;

    private final KakaoTokenFeignClient kakaoTokenFeignClient;
    private final KakaoUserInfoFeignClient kakaoUserInfoFeignClient;
    public String requestKakaoAccessToken(String authCode) {
        String body = "code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8)
                + "&client_id=" + clientId
                + "&redirect_uri=" + redirectUri
                + "&grant_type=" + grantType;

        KakaoAccessTokenResponseDTO response = kakaoTokenFeignClient.requestAccessToken(
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                body
        );

        log.info("Kakao AccessToken : {} // {}", response.tokenType(), response.accessToken());

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE))
                .accessToken();
    }

    public KakaoAccountResponseDTO requestKakaoAccount(String accessToken) {
        return kakaoUserInfoFeignClient.requestKakaoAccount(
                "Bearer " + accessToken,
                "application/x-www-form-urlencoded; charset=utf-8"
        );
    }
}
