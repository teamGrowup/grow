package org.boot.growup.source.admin.service;

import org.boot.growup.common.jwt.TokenDTO;
import org.boot.growup.source.admin.dto.AdminSignInRequestDTO;
import org.springframework.stereotype.Service;

@Service
public interface AdminService {
    TokenDTO signIn(AdminSignInRequestDTO request);
}
