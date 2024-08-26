package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.ProductOption;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductOptionRepository extends JpaRepository<ProductOption, Long> {
}
