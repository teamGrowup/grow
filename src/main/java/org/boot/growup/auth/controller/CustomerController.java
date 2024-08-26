package org.boot.growup.auth.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.auth.model.dto.response.*;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.service.impl.GoogleOauthServiceImpl;
import org.boot.growup.auth.service.impl.KakaoOauthServiceImpl;
import org.boot.growup.auth.service.impl.NaverOauthServiceImpl;
import org.boot.growup.auth.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;

    /**
     * [POST]
     * 구매자 이메일 회원가입
     * @header null
     * @body CustomerSignUpRequestDTO
     * @response void
     */
    @PostMapping("/email/register")
    public ResponseEntity<Void> signUp(@Valid @RequestBody CustomerSignUpRequestDTO request) {
        customerService.signUp(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * [POST]
     * 인증번호 전송 및 검증
     * @header null
     * @body PostAuthCodeRequestDTO
     * @response void
     */
    @PostMapping("/phone-numbers/validations/codes")
    public void postAuthCode(@RequestBody PostAuthCodeRequestDTO request) {
        customerService.postAuthCode(request);
    }

    /**
     * [POST]
     * 문자 인증완료 후 회원가입 중도포기
     * @header null
     * @body PostPhoneNumberRequestDTO
     * @response void
     */
    @PostMapping("/email/register/cancel")
    public void deletePhoneNumber(@RequestBody PostPhoneNumberRequestDTO request) {
        customerService.deletePhoneNumber(request);
    }

    /**
     * [POST]
     * 구매자 이메일 로그인
     * @header null
     * @body CustomerSignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/email/login")
    public BaseResponse<TokenDTO> signIn(@Valid @RequestBody CustomerSignInRequestDTO request) {
        TokenDTO loginResponse = customerService.signIn(request);
        return new BaseResponse<>(loginResponse);
    }

    /**
     * [POST]
     * 이메일 인증
     * @header null
     * @body String
     * @response String
     */
    @PostMapping("/email/validations")
    public BaseResponse<EmailCheckResponseDTO> checkEmail(
                @Valid @RequestBody EmailCheckRequestDTO request) throws MessagingException {
        EmailCheckResponseDTO response = customerService.checkEmail(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 구글 로그인 Oauth2.0
     * @header null
     * @body Oauth2SignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/google")
    public BaseResponse<TokenDTO> signInGoogle(@Valid @RequestBody Oauth2SignInRequestDTO request) {
        TokenDTO response = customerService.signInGoogle(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 구글 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body Oauth2AdditionalInfoRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/google/additional-infos")
    public BaseResponse<TokenDTO> signInGoogleAdditional(@Valid @RequestBody Oauth2AdditionalInfoRequestDTO request) {
        TokenDTO response = customerService.signInGoogleAdditional(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 카카오 로그인 Oauth2.0
     * @header null
     * @body Oauth2SignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/kakao")
    public BaseResponse<TokenDTO> signInKakao(@Valid @RequestBody Oauth2SignInRequestDTO request) {
        TokenDTO response = customerService.signInKakao(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 카카오 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body Oauth2AdditionalInfoRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/kakao/additional-infos")
    public BaseResponse<TokenDTO> signInKakaoAdditional(@Valid @RequestBody Oauth2AdditionalInfoRequestDTO request) {
        TokenDTO response = customerService.signInKakaoAdditional(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 네이버 로그인 Oauth2.0
     * @header null
     * @body Oauth2SignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/naver")
    public BaseResponse<TokenDTO> signInNaver(@Valid @RequestBody Oauth2SignInRequestDTO request) {
        TokenDTO response = customerService.signInNaver(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 네이버 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body Oauth2AdditionalInfoRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/naver/additional-infos")
    public BaseResponse<TokenDTO> signInNaverAdditional(@Valid @RequestBody Oauth2AdditionalInfoRequestDTO request) {
        TokenDTO response = customerService.signInNaverAdditional(request);
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 전화번호 인증 요청
     * @header null
     * @body PostPhoneNumberRequestDTO
     * @response void
     */
    @PostMapping("/phone-numbers/validations")
    public void postPhoneNumber(@RequestBody PostPhoneNumberRequestDTO request) {
        customerService.postPhoneNumber(request);
    }

    /**
     * [GET]
     * 마이페이지 구매자 정보 조회
     * @header Customer's AccessToken
     * @response GetCustomerInfoResponseDTO
     */
    @GetMapping("/mypages")
    public BaseResponse<GetCustomerInfoResponseDTO> getCustomerInfo() {
        return null;
    }
}
