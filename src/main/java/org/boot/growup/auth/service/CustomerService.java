package org.boot.growup.auth.service;

import jakarta.mail.MessagingException;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.model.TokenDTO;
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
    (회원가입) 전화번호 인증요청
     */
    void postPhoneNumberForRegister(PostPhoneNumberForRegisterRequestDTO request);

    /*
    (공통) 전화번호 인증요청
     */
    void postPhoneNumber(String phoneNumber);

    /*
    전화번호 인증 > 인증번호 검증
     */
    void postAuthCode(PostAuthCodeRequestDTO request);

    /*
    전화번호 인증정보 제거
     */
    void deletePhoneNumber(PostPhoneNumberForRegisterRequestDTO request);

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
    TokenDTO signInGoogle(Oauth2SignInRequestDTO request);

    /*
    신규 가입자 구글 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInGoogleAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    카카오 Oauth2.0 로그인
     */
    TokenDTO signInKakao(Oauth2SignInRequestDTO request);

    /*
    신규 가입자 카카오 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInKakaoAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    네이버 Oauth2.0 로그인
     */
    TokenDTO signInNaver(Oauth2SignInRequestDTO request);

    /*
    신규 가입자 네이버 Oauth2.0 로그인 > 추가 정보 입력
     */
    TokenDTO signInNaverAdditional(Oauth2AdditionalInfoRequestDTO request);

    /*
    현재 로그인한 소비자 조회
     */
    Customer getCurrentCustomer();

    /*
    Customer UserDetailsService
     */
    UserDetails loadUserByUsernameAndProvider(String username, Provider provider) throws UsernameNotFoundException;
}
