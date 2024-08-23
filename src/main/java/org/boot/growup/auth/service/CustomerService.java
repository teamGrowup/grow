package org.boot.growup.auth.service;

import jakarta.mail.MessagingException;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.model.dto.response.GoogleAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.KakaoAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.NaverAccountResponseDTO;
import org.boot.growup.auth.model.dto.response.EmailCheckResponseDTO;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

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
    TokenDTO signInGoogleAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    카카오 Oauth2.0 로그인
     */
    TokenDTO signInKakao(KakaoAccountResponseDTO kakaoAccount);

    /*
    신규 가입자 카카오 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInKakaoAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    네이버 Oauth2.0 로그인
     */
    TokenDTO signInNaver(NaverAccountResponseDTO naverAccount);

    /*
    신규 가입자 네이버 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInNaverAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    전화번호 인증요청
     */
    void postPhoneNumber(PostPhoneNumberRequestDTO request);

    /*
    전화번호 인증 > 인증번호 검증
     */
    void postAuthCode(PostAuthCodeRequestDTO request);

    /*
    현재 로그인한 소비자 조회
     */
    Customer getCurrentCustomer();

    /*
    전화번호 인증정보 제거
     */
    void deletePhoneNumber(PostPhoneNumberRequestDTO request);

    /*
    Customer UserDetailsService
     */
    UserDetails loadUserByUsernameAndProvider(String username, Provider provider) throws UsernameNotFoundException;
}
