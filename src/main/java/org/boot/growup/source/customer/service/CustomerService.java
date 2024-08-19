package org.boot.growup.source.customer.service;

import jakarta.mail.MessagingException;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.boot.growup.source.customer.dto.request.*;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.springframework.stereotype.Service;

@Service
public interface CustomerService {
    /*
    구매자 이메일 회원가입
     */
    void signUp(CustomerSignUpRequestDTO request);

    /*
    구매자 이메일 로그인
     */
    TokenDTO signIn(CustomerSignInRequestDTO request);

    /*
    이메일 인증
     */
    EmailCheckResponseDTO checkEmail(EmailCheckRequestDTO request) throws MessagingException;

    /*
    구글 Oauth2.0 로그인
     */
    TokenDTO signInGoogle(GoogleAccountResponseDTO googleAccount);

    /*
    신규 가입자 구글 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInGoogleAdditional(GoogleAdditionalInfoRequestDTO request);

    /*
    카카오 Oauth2.0 로그인
     */
    TokenDTO signInKakao(KakaoAccountResponseDTO kakaoAccount);

    /*
    신규 가입자 카카오 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInKakaoAdditional(KakaoAdditionalInfoRequestDTO request);

    /*
    네이버 Oauth2.0 로그인
     */
    TokenDTO signInNaver(NaverAccountResponseDTO naverAccount);

    /*
    신규 가입자 네이버 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInNaverAdditional(NaverAdditionalInfoRequestDTO request);

    /*
    현재 로그인한 소비자 조회
     */
    Customer getCurrentCustomer();
}
