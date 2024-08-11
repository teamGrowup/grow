package org.boot.growup.common.oauth2.naver;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.call.NaverTokenFeignClient;
import org.boot.growup.common.oauth2.naver.call.NaverUserInfoFeignClient;
import org.boot.growup.common.oauth2.naver.dto.NaverAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE;

@Component
@Slf4j
@RequiredArgsConstructor
@Getter
public class NaverOauthService {
    @Value("${oauth2.naver.client-id}")
    private String clientId;

    @Value("${oauth2.naver.client-secret}")
    private String clientSecret;

    @Value("${oauth2.naver.redirect-uri}")
    private String redirectUri;

    @Value("${oauth2.naver.grant-type}")
    private String grantType;

    private final NaverTokenFeignClient naverTokenFeignClient;
    private final NaverUserInfoFeignClient naverUserInfoFeignClient;
    public String requestNaverAccessToken(String authCode) {
        NaverAccessTokenResponseDTO response = naverTokenFeignClient.requestAccessToken(
                clientId,
                clientSecret,
                "authorization_code",
                URLEncoder.encode(authCode, StandardCharsets.UTF_8)
        );

        log.info("Naver AccessToken info: {}", response);

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE))
                .accessToken();
    }

    public NaverAccountResponseDTO requestNaverAccount(String accessToken) {
        return naverUserInfoFeignClient.requestNaverAccount(
                "Bearer " + accessToken
        );
    }
}
