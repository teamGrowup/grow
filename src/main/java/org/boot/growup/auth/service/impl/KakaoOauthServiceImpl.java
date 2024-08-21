package org.boot.growup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.model.KakaoClient;
import org.boot.growup.auth.model.dto.response.KakaoAccessTokenResponseDTO;
import org.boot.growup.auth.service.KakaoOauthService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.auth.controller.KakaoTokenFeignClient;
import org.boot.growup.auth.controller.KakaoUserInfoFeignClient;
import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.constant.ErrorCode.NOT_FOUND_KAKAO_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoOauthServiceImpl implements KakaoOauthService {
    private final KakaoClient kakaoClient;
    private final KakaoTokenFeignClient kakaoTokenFeignClient;
    private final KakaoUserInfoFeignClient kakaoUserInfoFeignClient;

    @Override
    public String requestKakaoAccessToken(String authCode) {
        String body = "code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8)
                + "&client_id=" + kakaoClient.getClientId()
                + "&redirect_uri=" + kakaoClient.getRedirectUri()
                + "&grant_type=" + kakaoClient.getGrantType();

        KakaoAccessTokenResponseDTO response = kakaoTokenFeignClient.requestAccessToken(
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                body
        );

        log.info("Kakao AccessToken : {} {}", response.getTokenType(), response.getAccessToken());

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_KAKAO_ACCESS_TOKEN_RESPONSE))
                .getAccessToken();
    }

    @Override
    public KakaoAccountResponseDTO requestKakaoAccount(String accessToken) {
        return kakaoUserInfoFeignClient.requestKakaoAccount(
                "Bearer " + accessToken,
                "application/x-www-form-urlencoded; charset=utf-8"
        );
    }
}
