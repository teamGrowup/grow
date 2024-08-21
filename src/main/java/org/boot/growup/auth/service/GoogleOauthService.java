package org.boot.growup.auth.service;

import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;

public interface GoogleOauthService {
    /*
    구글 서버에 AccessToken 발급 요청
     */
    String requestGoogleAccessToken(String authCode);

    /*
    구글 서버에 사용자 정보 발급 요청
     */
    GoogleAccountResponseDTO requestGoogleAccount(String accessToken);
}
