package org.boot.growup.source.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.admin.dto.AdminSignInRequestDTO;
import org.boot.growup.source.admin.service.AdminService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
