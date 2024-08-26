package org.boot.growup.auth.service;

import jakarta.mail.MessagingException;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.auth.model.dto.response.GetCustomerInfoResponseDTO;
import org.boot.growup.auth.model.dto.response.GetIsValidEmailResponseDTO;
import org.boot.growup.common.constant.Provider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

    /*
    마이페이지 조회
     */
    GetCustomerInfoResponseDTO getCustomerInfo();

    /*
    이메일 전송
     */
    void postEmail(String email) throws MessagingException;

    /*
    이메일 인증코드 검증
     */
    void postEmailAuthCode(PostEmailAuthCodeRequestDTO request);

    /*
    이메일 존재여부 검증
     */
    void postEmailExistence(PostEmailRequestDTO request);

    /*
    비밀번호 변경
     */
    void patchPassword(PatchPasswordRequestDTO request);


    /*
    프로필 이미지 변경
     */
    void patchProfile(MultipartFile multipartFile) throws IOException;

    /*
    이메일 인증여부 조회
     */
    GetIsValidEmailResponseDTO getIsValidEmail();

    /*
    이메일 알림 허가/거부
     */
    void patchAgreementSendEmail(PatchAgreementSendEmailRequestDTO request);

    /*
    문자 알림 허가/거부
     */
    void patchAgreementSendSms(PatchAgreementSendSmsRequestDTO request);

    /*
    배송지 추가
     */
    void postAddress(PostAddressRequestDTO request);
}
