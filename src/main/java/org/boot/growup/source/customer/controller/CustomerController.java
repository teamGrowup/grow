package org.boot.growup.source.customer.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<BaseResponse<EmailCheckResponseDTO>> emailCheck(
                @Valid @RequestBody EmailCheckRequestDTO request) throws MessagingException {
        EmailCheckResponseDTO response = customerService.emailCheck(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }

    /**
     * [POST]
     * 구글 로그인 Oauth2.0
     * @header null
     * @body GoogleSignInRequestDTO
     * @response GoogleSignInResponseDTO
     */
    @PostMapping("/oauth/google")
    public ResponseEntity<BaseResponse<TokenDto>> googleSignIn(@RequestBody GoogleSignInRequestDTO request) {
        /* 인가코드를 받아서 Google에 AccessToken 요청 -> 받은 AccessToken으로 Google 사용자 정보 요청 */
        String accessToken = googleOauthService.requestGoogleAccessToken(request.getAuthCode());
        GoogleAccountResponseDTO googleUser = googleOauthService.requestGoogleAccount(accessToken);
        log.info("Google User : {}", googleUser);
        /* Google 사용자 정보를 userService에서 회원가입 및 로그인 처리 */
        return null;
    }

    /**
     * [POST]
     * 카카오 로그인 Oauth2.0
     * @header null
     * @body KakaoSignInRequestDTO
     * @response
     */
    @PostMapping("/oauth/kakao")
    public ResponseEntity<BaseResponse<TokenDto>> kakaoSignIn(@RequestBody KakaoSignInRequestDTO request) {
        String accessToken = kakaoOauthService.requestKakaoAccessToken(request.getAuthCode());
        KakaoAccountResponseDTO kakaoUser = kakaoOauthService.requestKakaoAccount(accessToken);
        log.info("Kakao User : {}", kakaoUser);
        return null;
    }

    /**
     * [POST]
     * 네이버 로그인 Oauth2.0
     * @header null
     * @body NaverSignInRequestDTO
     * @response
     */
    @PostMapping("/oauth/naver")
    public ResponseEntity<BaseResponse<TokenDto>> naverSignIn(@RequestBody NaverSignInRequestDTO request) {
        String accessToken = naverOauthService.requestNaverAccessToken(request.getAuthCode());
        NaverAccountResponseDTO naverUser = naverOauthService.requestNaverAccount(accessToken);
        log.info("Naver User : {}", naverUser);
        return null;
    }
}
