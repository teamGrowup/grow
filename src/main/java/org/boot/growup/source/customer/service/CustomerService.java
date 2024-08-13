package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.boot.growup.source.customer.dto.request.*;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    void signUp(CustomerSignUpRequestDTO request);
    TokenDTO signIn(CustomerSignInRequestDTO request);
    EmailCheckResponseDTO emailCheck(EmailCheckRequestDTO request) throws MessagingException;
    TokenDTO signInGoogle(GoogleAccountResponseDTO googleAccount);
    TokenDTO signInGoogleAdditional(GoogleAdditionalInfoRequestDTO request);
    TokenDTO signInKakao(KakaoAccountResponseDTO kakaoAccount);
    TokenDTO signInKakaoAdditional(KakaoAdditionalInfoRequestDTO request);
    TokenDTO signInNaver(NaverAccountResponseDTO naverAccount);
    TokenDTO signInNaverAdditional(NaverAdditionalInfoRequestDTO request);
}
