package org.boot.growup.auth.service;

import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;

public interface NaverOauthService {
    String requestNaverAccessToken(String authCode);
    NaverAccountResponseDTO requestNaverAccount(String accessToken);
}
