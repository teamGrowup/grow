package org.boot.growup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.model.dto.response.NaverAccessTokenResponseDTO;
import org.boot.growup.auth.service.NaverOauthService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.auth.client.NaverTokenFeignClient;
import org.boot.growup.auth.client.NaverUserInfoFeignClient;
import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;
import org.boot.growup.common.config.property.Oauth2Property;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.constant.ErrorCode.NOT_FOUND_NAVER_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class NaverOauthServiceImpl implements NaverOauthService {
    private final Oauth2Property oauth2Property;
    private final NaverTokenFeignClient naverTokenFeignClient;
    private final NaverUserInfoFeignClient naverUserInfoFeignClient;

    @Override
    public String requestNaverAccessToken(String authCode) {
        NaverAccessTokenResponseDTO response = naverTokenFeignClient.requestAccessToken(
                oauth2Property.getNaver().getClientId(),
                oauth2Property.getNaver().getClientSecret(),
                oauth2Property.getNaver().getGrantType(),
                URLEncoder.encode(authCode, StandardCharsets.UTF_8)
        );

        log.info("Naver AccessToken info: {}", response);

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_NAVER_ACCESS_TOKEN_RESPONSE))
                .getAccessToken();
    }

    @Override
    public NaverAccountResponseDTO requestNaverAccount(String accessToken) {
        return naverUserInfoFeignClient.requestNaverAccount("Bearer " + accessToken);
    }
}
