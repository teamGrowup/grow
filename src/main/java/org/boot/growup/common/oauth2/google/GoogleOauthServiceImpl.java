package org.boot.growup.common.oauth2.google;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.oauth2.google.client.GoogleTokenFeignClient;
import org.boot.growup.common.oauth2.google.client.GoogleUserInfoFeignClient;
import org.boot.growup.common.oauth2.google.dto.GoogleAccessTokenResponseDTO;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.error.ErrorCode.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GoogleOauthServiceImpl implements GoogleOauthService {
    private final GoogleClient googleClient;
    private final GoogleTokenFeignClient googleTokenFeignClient;
    private final GoogleUserInfoFeignClient googleUserInfoFeignClient;

    @Override
    public String requestGoogleAccessToken(String authCode) {
        String body = "code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8)
                    + "&client_id=" + googleClient.getGoogleClientId()
                    + "&client_secret=" + googleClient.getGoogleClientSecret()
                    + "&redirect_uri=" + googleClient.getGoogleCallbackUri()
                    + "&grant_type=" + googleClient.getGrantType();

        GoogleAccessTokenResponseDTO response = googleTokenFeignClient.requestAccessToken(
                MediaType.APPLICATION_FORM_URLENCODED_VALUE,
                body
        );

        log.info("Google AccessToken : {}", response);

        return Optional.of(response)
                .orElseThrow(() -> new BaseException(NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE))
                .getAccessToken();
    }

    @Override
    public GoogleAccountResponseDTO requestGoogleAccount(String accessToken) {
        return googleUserInfoFeignClient.requestGoogleAccount("Bearer " + accessToken);
    }
}
