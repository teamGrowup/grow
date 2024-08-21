package org.boot.growup.auth.persist.repository;

import org.boot.growup.auth.persist.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByEmail(String username);
}
