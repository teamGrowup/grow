package org.boot.growup.common.oauth2.kakao;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.oauth2.kakao.call.KakaoTokenFeignClient;
import org.boot.growup.common.oauth2.kakao.call.KakaoUserInfoFeignClient;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_KAKAO_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
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
