package org.boot.growup.auth.service;

import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;

public interface KakaoOauthService {
    /*
    카카오 서버에 AccessToken 발급 요청
     */
    String requestKakaoAccessToken(String authCode);

    /*
    카카오 서버에 사용자 정보 발급 요청
     */
    KakaoAccountResponseDTO requestKakaoAccount(String accessToken);
}
