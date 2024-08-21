package org.boot.growup.auth.service;

import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.model.dto.request.AdminSignInRequestDTO;
import org.boot.growup.auth.persist.entity.Admin;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    /*
    관리자 로그인
     */
    TokenDTO signIn(AdminSignInRequestDTO request);

    /*
    현재 로그인한 관리자 조회
     */
    Admin getCurrentAdmin();
}
