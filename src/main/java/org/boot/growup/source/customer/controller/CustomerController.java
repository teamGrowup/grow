package org.boot.growup.source.customer.controller;

import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.common.oauth2.google.GoogleAccountResponseDTO;
import org.boot.growup.common.oauth2.google.GoogleOauthService;
import org.boot.growup.source.customer.dto.request.CustomerSignInRequestDTO;
import org.boot.growup.source.customer.dto.request.CustomerSignUpRequestDTO;
import org.boot.growup.source.customer.dto.request.EmailCheckRequestDTO;
import org.boot.growup.source.customer.dto.request.GoogleSignInRequestDTO;
import org.boot.growup.source.customer.dto.response.EmailCheckResponseDTO;
import org.boot.growup.source.customer.dto.response.GoogleSignInResponseDTO;
import org.boot.growup.source.customer.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
@Slf4j
public class CustomerController {
    private final CustomerService customerService;
    private final GoogleOauthService googleOauthService;

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
     * @body EmailCheckRequestDTO
     * @response EmailCheckResponseDTO
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
     * 처음 로그인 시도 -> 구글 플랫폼을 통한 계정선택 후 받은 인가코드와 함께 API를 요청하게 된다.
     * 이후 로그인 시도 -> 구글 CI를 클릭했을 때 받은 인가코드와 함께 API를 요청하게 된다.
     * @header null
     * @body GoogleSignInRequestDTO
     * @response GoogleSignInResponseDTO
     */
    @PostMapping("/oauth/google")
    public ResponseEntity<BaseResponse<TokenDto>> googleSignIn(@RequestBody GoogleSignInRequestDTO request) {
        /* 인가코드를 받아서 Google에 AccessToken 요청 -> 받은 AccessToken으로 Google 사용자 정보 요청 */
        String accessToken = googleOauthService.requestGoogleAccessToken(request.authCode());
        GoogleAccountResponseDTO googleUser = googleOauthService.requestGoogleAccount(accessToken);
        log.info("Google User : {}", googleUser.email());
        /* Google 사용자 정보를 userService에서 회원가입 및 로그인 처리 */
        return null;
    }

    @GetMapping("/test/google")
    public ResponseEntity<Void> test(HttpServletResponse response) throws IOException {
        String redirectUrl = googleOauthService.sendAuthorizationRequest();
        response.sendRedirect(redirectUrl); // 브라우저를 리디렉션 시킴
        log.info("url : {}", redirectUrl);
        return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(redirectUrl)).build();
    }



}
