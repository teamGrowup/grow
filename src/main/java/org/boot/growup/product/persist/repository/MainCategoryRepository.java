package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
}