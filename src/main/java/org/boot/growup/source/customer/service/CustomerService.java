package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.boot.growup.source.customer.dto.request.*;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    void signUp(CustomerSignUpRequestDTO request);
    String encodingPassword(CustomerSignUpRequestDTO request);
    TokenDto signIn(CustomerSignInRequestDTO request);
    boolean checkPassword(String rawPassword, String encodedPassword);
    EmailCheckResponseDTO emailCheck(EmailCheckRequestDTO request) throws MessagingException;
    TokenDto signInGoogle(GoogleAccountResponseDTO googleAccount);
    TokenDto signInGoogleAdditional(GoogleAdditionalInfoRequestDTO request);
    TokenDto signInKakao(KakaoAccountResponseDTO kakaoAccount);
    TokenDto signInKakaoAdditional(KakaoAdditionalInfoRequestDTO request);
    TokenDto signInNaver(NaverAccountResponseDTO naverAccount);
    TokenDto signInNaverAdditional(NaverAdditionalInfoRequestDTO request);
}
