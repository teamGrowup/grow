package org.boot.growup.product.persist.repository;

import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.persist.entity.Brand;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BrandRepository extends JpaRepository<Brand, Long> {
    Optional<Brand> findByName(String name);
    Optional<Brand> findBySeller_Id(Long id);
    List<Brand> findByAuthorityStatus(AuthorityStatus authorityStatus, Pageable pageable);
}
