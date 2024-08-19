package org.boot.growup.common.oauth2.google;

import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.springframework.stereotype.Service;

@Service
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
