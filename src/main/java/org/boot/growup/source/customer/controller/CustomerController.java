package org.boot.growup.source.customer.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.common.oauth2.google.dto.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.google.GoogleOauthService;
import org.boot.growup.common.oauth2.kakao.KakaoOauthService;
import org.boot.growup.common.oauth2.kakao.dto.KakaoAccountResponseDTO;
import org.boot.growup.common.oauth2.naver.NaverOauthService;
import org.boot.growup.common.oauth2.naver.dto.NaverAccountResponseDTO;
import org.boot.growup.source.customer.dto.request.*;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.boot.growup.source.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    private final GoogleOauthService googleOauthService;
    private final KakaoOauthService kakaoOauthService;
    private final NaverOauthService naverOauthService;

    /**
     * [POST]
     * 구매자 이메일 회원가입
     * @header null
     * @body CustomerSignUpRequestDTO
     * @response void
     */
    @PostMapping("/email/register")
    public void signUp(@Valid @RequestBody CustomerSignUpRequestDTO request) {
        customerService.signUp(request);
    }

    /**
     * [POST]
     * 구매자 이메일 로그인
     * @header null
     * @body CustomerSignInRequestDTO
     * @response TokenDto
     */
    @PostMapping("/email/login")
    public ResponseEntity<BaseResponse<TokenDto>> signIn(@Valid @RequestBody CustomerSignInRequestDTO request) {
        TokenDto loginResponse = customerService.signIn(request);
        return ResponseEntity.ok(new BaseResponse<>(loginResponse));
    }

    /**
     * [POST]
     * 이메일 인증
     * @header null
     * @body String
     * @response String
     */
    @PostMapping("/email/validation")
    public ResponseEntity<BaseResponse<EmailCheckResponseDTO>> checkEmail(
                @Valid @RequestBody EmailCheckRequestDTO request) throws MessagingException {
        EmailCheckResponseDTO response = customerService.emailCheck(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 구글 로그인 Oauth2.0
     * @header null
     * @body GoogleSignInRequestDTO
     * @response TokenDto
     */
    @PostMapping("/oauth/google")
    public ResponseEntity<BaseResponse<TokenDto>> signInGoogle(@Valid @RequestBody GoogleSignInRequestDTO request) {
        /* 인가코드를 받아서 Google에 AccessToken 요청 -> 받은 AccessToken으로 Google 사용자 정보 요청 */
        String accessToken = googleOauthService.requestGoogleAccessToken(request.getAuthCode());
        GoogleAccountResponseDTO googleAccount = googleOauthService.requestGoogleAccount(accessToken);
        log.info("Google User : {}", googleAccount);

        /* Google 사용자 정보를 userService에서 회원가입 및 로그인 처리 */
        TokenDto response = customerService.signInGoogle(googleAccount);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 구글 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body GoogleAdditionalInfoRequestDTO
     * @response TokenDto
     */
    @PostMapping("/oauth/google/additional-info")
    public ResponseEntity<BaseResponse<TokenDto>> signInGoogleAdditional(
                @Valid @RequestBody GoogleAdditionalInfoRequestDTO request) {
        TokenDto response = customerService.signInGoogleAdditional(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 카카오 로그인 Oauth2.0
     * @header null
     * @body KakaoSignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/kakao")
    public ResponseEntity<BaseResponse<TokenDto>> signInKakao(@Valid @RequestBody KakaoSignInRequestDTO request) {
        String accessToken = kakaoOauthService.requestKakaoAccessToken(request.getAuthCode());
        KakaoAccountResponseDTO kakaoAccount = kakaoOauthService.requestKakaoAccount(accessToken);
        log.info("Kakao User : {}", kakaoAccount);

        TokenDto response = customerService.signInKakao(kakaoAccount);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 카카오 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body KakaoAdditionalInfoRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/oauth/kakao/additional-info")
    public ResponseEntity<BaseResponse<TokenDto>> signInKakaoAdditional(
            @Valid @RequestBody KakaoAdditionalInfoRequestDTO request) {
        TokenDto response = customerService.signInKakaoAdditional(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 네이버 로그인 Oauth2.0
     * @header null
     * @body NaverSignInRequestDTO
     * @response
     */
    @PostMapping("/oauth/naver")
    public ResponseEntity<BaseResponse<TokenDto>> signInNaver(@Valid @RequestBody NaverSignInRequestDTO request) {
        String accessToken = naverOauthService.requestNaverAccessToken(request.getAuthCode());
        NaverAccountResponseDTO naverAccount = naverOauthService.requestNaverAccount(accessToken);
        log.info("Naver User : {}", naverAccount);

        TokenDto response = customerService.signInNaver(naverAccount);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 네이버 로그인 Oauth2.0 - 초기 사용자 회원가입 처리
     * @header null
     * @body NaverAdditionalInfoRequestDTO
     * @response
     */
    @PostMapping("/oauth/naver/additional-info")
    public ResponseEntity<BaseResponse<TokenDto>> signInNaverAdditional(
                @Valid @RequestBody NaverAdditionalInfoRequestDTO request) {
        TokenDto response = customerService.signInNaverAdditional(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    @GetMapping("/role")
    public void roleTest() {
        Collection<? extends GrantedAuthority> role = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        log.info("Role : {}",role);
    }
}
