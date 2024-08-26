package org.boot.growup.auth.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.model.dto.request.*;
import org.boot.growup.auth.model.dto.response.*;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    @PostMapping("/email/registers")
    public ResponseEntity<Void> signUp(@Valid @RequestBody CustomerSignUpRequestDTO request) {
        customerService.signUp(request);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * [POST]
     * (회원가입) 전화번호 인증 요청
     * @header null
     * @body PostPhoneNumberForRegisterRequestDTO
     * @response void
     */
    @PostMapping("/phone-numbers/validations/registers")
    public void postPhoneNumberForRegister(@RequestBody PostPhoneNumberForRegisterRequestDTO request) {
        customerService.postPhoneNumberForRegister(request);
    }

    /**
     * [POST]
     * (공통) 전화번호 인증 요청
     * @header null
     * @body PostPhoneNumberRequestDTO
     * @response void
     */
    @PostMapping("/phone-numbers/validations")
    public void postPhoneNumber(@RequestBody PostPhoneNumberRequestDTO request) {
        customerService.postPhoneNumber(request.getPhoneNumber());
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
     * 문자 인증완료 후 회원가입 중도포기 상황
     * @header null
     * @body PostPhoneNumberForRegisterRequestDTO
     * @response void
     */
    @PostMapping("/email/register/cancel")
    public void deletePhoneNumber(@RequestBody PostPhoneNumberForRegisterRequestDTO request) {
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
     * [GET]
     * 마이페이지 구매자 정보 조회
     * @header Customer's AccessToken
     * @body null
     * @response GetCustomerInfoResponseDTO
     */
    @GetMapping("/mypages")
    public BaseResponse<GetCustomerInfoResponseDTO> getCustomerInfo() {
        GetCustomerInfoResponseDTO response = customerService.getCustomerInfo();
        return new BaseResponse<>(response);
    }

    /**
     * [POST]
     * 이메일 전송
     * @header Customer's AccessToken
     * @body PostEmailRequestDTO
     * @response void
     */
    @PostMapping("/mypages/emails")
    public void postEmail(@Valid @RequestBody PostEmailRequestDTO request) throws MessagingException {
        customerService.postEmail(request.getEmail());
    }

    /**
     * [POST]
     * 이메일 인증코드 검증
     * @header Customer's AccessToken
     * @body PostEmailAuthCodeRequestDTO
     * @response void
     */
    @PostMapping("/mypages/emails/authcodes")
    public void postEmailAuthCode(@Valid @RequestBody PostEmailAuthCodeRequestDTO request) {
        customerService.postEmailAuthCode(request);
    }

    /**
     * [POST]
     * 이메일 존재여부 검증
     * @header Customer's AccessToken
     * @body PostEmailRequestDTO
     * @response void
     */
    @PostMapping("/mypages/emails/existences")
    public void postEmailExistence(@Valid @RequestBody PostEmailRequestDTO request) {
        customerService.postEmailExistence(request);
    }

    /**
     * [PATCH]
     * 비밀번호 변경
     * @header Customer's AccessToken
     * @body PatchPasswordRequestDTO
     * @response void
     */
    @PatchMapping("/mypages/passwords")
    public void patchPassword(@Valid @RequestBody PatchPasswordRequestDTO request) {
        customerService.patchPassword(request);
    }

    /**
     * [PATCH]
     * 프로필 사진 변경
     * @header Customer's AccessToken
     * @body PatchProfileDTO
     * @response void
     */
    @PatchMapping(value = "/mypages/profiles", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void patchProfile(@RequestPart(value = "profile") MultipartFile multipartFile) throws IOException {
        customerService.patchProfile(multipartFile);
    }

    /**
     * [GET]
     * 이메일 인증여부 조회
     * @header Customer's AccessToken
     * @body null
     * @response GetIsValidEmailResponseDTO
     */
    @GetMapping("/mypages/emails/is-valid")
    public BaseResponse<GetIsValidEmailResponseDTO> getIsValidEmail() {
        GetIsValidEmailResponseDTO response = customerService.getIsValidEmail();
        return new BaseResponse<>(response);
    }


    /**
     * [PATCH]
     * 이메일 알림 허가/거부
     * @header Customer's AccessToken
     * @body PatchAgreementSendEmailRequestDTO
     * @response void
     */
    @PatchMapping("/mypages/emails/agreements")
    public void patchAgreementSendEmail(@RequestBody PatchAgreementSendEmailRequestDTO request) {
        log.info("Request Body: {}", request); // 전체 요청 바디 확인
        log.info("isAgreeSendEmail : {}", request.isAgreementSendEmail());
        customerService.patchAgreementSendEmail(request);
    }


    /**
     * [PATCH]
     * 문자 알림 허가/거부
     * @header Customer's AccessToken
     * @body PatchAgreementSendSmsRequestDTO
     * @response void
     */
    @PatchMapping("/mypages/sms/agreements")
    public void patchAgreementSendSms(@RequestBody PatchAgreementSendSmsRequestDTO request) {
        customerService.patchAgreementSendSms(request);
    }


    /**
     * [POST]
     * 배송지 추가
     * @header Customer's AccessToken
     * @body PostAddressRequestDTO
     * @response void
     */

    /**
     * [GET]
     * 배송지 조회 (여러건)
     * @header Customer's AccessToken
     * @body null
     * @response GetAddressResponseDTO
     */

    /**
     * [PATCH]
     * 배송지 수정
     * @header Customer's AccessToken
     * @body PatchAddressRequestDTO
     * @path addressIdx
     * @response void
     */

    /**
     * [DELETE]
     * 배송지 삭제
     * @header Customer's AccessToken
     * @body DeleteAddressRequestDTO
     * @response void
     */
}
