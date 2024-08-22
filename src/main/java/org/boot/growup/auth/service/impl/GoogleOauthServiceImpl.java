package org.boot.growup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.client.GoogleTokenFeignClient;
import org.boot.growup.auth.client.GoogleUserInfoFeignClient;
import org.boot.growup.auth.service.GoogleOauthService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.auth.model.dto.response.GoogleAccessTokenResponseDTO;
import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
import org.boot.growup.common.model.Oauth2Property;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.boot.growup.common.constant.ErrorCode.NOT_FOUND_GOOGLE_ACCESS_TOKEN_RESPONSE;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleOauthServiceImpl implements GoogleOauthService {
    private final Oauth2Property oauth2Property;
    private final GoogleTokenFeignClient googleTokenFeignClient;
    private final GoogleUserInfoFeignClient googleUserInfoFeignClient;

    @Override
    public String requestGoogleAccessToken(String authCode) {
        String body = "code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8)
                    + "&client_id=" + oauth2Property.getGoogle().getClient().getId()
                    + "&client_secret=" + oauth2Property.getGoogle().getClient().getSecret()
                    + "&redirect_uri=" + oauth2Property.getGoogle().getCallbackUri()
                    + "&grant_type=" + oauth2Property.getGoogle().getGrantType();

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
