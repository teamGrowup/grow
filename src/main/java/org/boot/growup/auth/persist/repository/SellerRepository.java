package org.boot.growup.auth.persist.repository;

import org.boot.growup.auth.persist.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellerRepository extends JpaRepository<Seller, Long> {
    Optional<Seller> findByCpEmail(String username);
}
