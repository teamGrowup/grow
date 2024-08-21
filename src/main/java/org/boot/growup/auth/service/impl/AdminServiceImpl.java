package org.boot.growup.auth.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.service.AdminService;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.common.model.TokenDTO;
import org.boot.growup.auth.service.UserService;
import org.boot.growup.auth.model.dto.request.AdminSignInRequestDTO;
import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.auth.persist.repository.AdminRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AdminRepository adminRepository;

    @Transactional
    @Override
    public TokenDTO signIn(AdminSignInRequestDTO request) {
        UserDetails userDetails = userService.loadUserByUsername(request.getEmail());

        if(!checkPassword(request.getPassword(), userDetails.getPassword())){ // 비밀번호 비교
            throw new BaseException(ErrorCode.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");
        }

        return jwtTokenProvider.generateToken(userDetails.getUsername(),userDetails.getAuthorities());
    }

    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public Admin getCurrentAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        log.info(user.getUsername(), user.getAuthorities().toString());
        if (!ObjectUtils.isEmpty(user)) {
            String useremail = user.getUsername();
            String authority = user.getAuthorities().stream().findFirst().orElseThrow(
                    () -> new BaseException(ErrorCode.INTERNAL_SERVER_ERROR)
            ).toString();

            log.info("useremail : {} | authority : {}", useremail, authority);

            if (authority.equals(Role.ADMIN.getKey())) {
                return adminRepository.findByEmail(useremail).orElseThrow(
                        () -> new BaseException(ErrorCode.ADMIN_NOT_FOUND)
                );
            }

            throw new BaseException(ErrorCode.ACCESS_DENIED);
        }

        throw new BaseException(ErrorCode.ACCESS_DENIED);
    }

}
