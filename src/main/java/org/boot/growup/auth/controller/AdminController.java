package org.boot.growup.auth.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.model.dto.request.AdminSignInRequestDTO;
import org.boot.growup.auth.service.AdminService;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    /**
     * [POST]
     * 관리자 이메일 로그인
     * @header null
     * @body AdminSignInRequestDTO
     * @response TokenDTO
     */
    @PostMapping("/login")
    public BaseResponse<TokenDTO> signIn(@RequestBody AdminSignInRequestDTO request) {
        TokenDTO response = adminService.signIn(request);
        return new BaseResponse<>(response);
    }


    @GetMapping("/logout")
    public void logout() {

    }


}
