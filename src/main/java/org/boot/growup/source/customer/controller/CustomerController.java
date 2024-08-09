package org.boot.growup.source.customer.controller;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignInRequestDTO;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignUpRequestDTO;
import org.boot.growup.source.customer.dto.request.EmailValidationRequestDTO;
import org.boot.growup.source.customer.dto.response.EmailValidationResponseDTO;
import org.boot.growup.source.customer.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    /**
     * [POST]
     * 구매자 이메일 회원가입
     * @header null
     * @body CustomerEmailSignUpRequestDTO
     * @response void
     */
    @PostMapping("/email/register")
    public void signUp(@Valid @RequestBody CustomerEmailSignUpRequestDTO request) {
        customerService.signUp(request);
    }

    /**
     * [POST]
     * 구매자 이메일 로그인
     * @header null
     * @body CustomerEmailSignInRequestDTO
     * @response TokenDto
     */
    @PostMapping("/email/login")
    public ResponseEntity<BaseResponse<TokenDto>> signIn(@Valid @RequestBody CustomerEmailSignInRequestDTO request) {
        TokenDto loginResponse = customerService.signIn(request);
        return ResponseEntity.ok(new BaseResponse<>(loginResponse));
    }

    /**
     * [POST]
     * 이메일 인증
     * @header null
     * @body EmailValidationRequestDTO
     * @response EmailValidationResponseDTO
     */
    @PostMapping("/email/validation")
    public ResponseEntity<BaseResponse<EmailValidationResponseDTO>> emailCheck(
                @Valid @RequestBody EmailValidationRequestDTO request) throws MessagingException {
        EmailValidationResponseDTO response = customerService.emailCheck(request);
        return ResponseEntity.ok(new BaseResponse<>(response));
    }
}
