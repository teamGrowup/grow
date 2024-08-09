package org.boot.growup.source.customer.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDto;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignInRequest;
import org.boot.growup.source.customer.dto.request.CustomerEmailSignUpRequest;
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
     * @body CustomerEmailSignUpRequest
     * @response void
     */
    @PostMapping("/email/register")
    public void signUp(@Valid @RequestBody CustomerEmailSignUpRequest request) {
        customerService.signUp(request);
    }

    /**
     * [POST]
     * 구매자 이메일 로그인
     * @header null
     * @body CustomerEmailSignInRequest
     * @response TokenDto
     */
    @PostMapping("/email/login")
    public ResponseEntity<BaseResponse<TokenDto>> signIn(@Valid @RequestBody CustomerEmailSignInRequest request) {
        TokenDto loginResponse = customerService.signIn(request);
        return ResponseEntity.ok(new BaseResponse<>(loginResponse));
    }
}
