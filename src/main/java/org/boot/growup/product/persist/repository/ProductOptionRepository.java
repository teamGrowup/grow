package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
    List<ProductOption> findByProductId(Long productId); // 상품 ID로 옵션 조회
}

