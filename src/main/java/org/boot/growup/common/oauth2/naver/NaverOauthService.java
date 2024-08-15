package org.boot.growup.common.oauth2.naver;

import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface NaverOauthService {
    String requestNaverAccessToken(String authCode);
    NaverAccountResponseDTO requestNaverAccount(String accessToken);
}
