package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.Seller;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {
}