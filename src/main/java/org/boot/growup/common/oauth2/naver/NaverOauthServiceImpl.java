package org.boot.growup.common.oauth2.naver;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.oauth2.naver.client.NaverTokenFeignClient;
import org.boot.growup.common.oauth2.naver.client.NaverUserInfoFeignClient;
import org.boot.growup.common.oauth2.naver.dto.NaverAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_NAVER_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NaverOauthServiceImpl implements NaverOauthService {
    private final NaverClient naverClient;
    private final NaverTokenFeignClient naverTokenFeignClient;
    private final NaverUserInfoFeignClient naverUserInfoFeignClient;

    @Override
    public String requestNaverAccessToken(String authCode) {
        NaverAccessTokenResponseDTO response = naverTokenFeignClient.requestAccessToken(
                naverClient.getClientId(),
                naverClient.getClientSecret(),
                naverClient.getGrantType(),
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
