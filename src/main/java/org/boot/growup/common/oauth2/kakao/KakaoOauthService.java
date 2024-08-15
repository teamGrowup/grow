package org.boot.growup.common.oauth2.kakao;

import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.springframework.stereotype.Service;

@Service
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
