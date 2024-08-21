package org.boot.growup.source.admin.service;

import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.admin.dto.AdminSignInRequestDTO;
import org.boot.growup.source.admin.persist.entity.Admin;
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
