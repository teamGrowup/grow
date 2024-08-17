package org.boot.growup.source.admin.persist.repository;

import org.boot.growup.source.admin.persist.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByUid(String username);
}
