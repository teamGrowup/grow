package org.boot.growup.common.dataloader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.admin.persist.repository.AdminRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestDataLoader {
    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        adminInit();
    }

    public void adminInit(){
        Admin admin = Admin.builder()
                .uid("root")
                .password(passwordEncoder.encode("1234"))
                .balance(0)
                .role(Role.ADMIN)
                .build();
        adminRepository.save(admin);
    }
}
