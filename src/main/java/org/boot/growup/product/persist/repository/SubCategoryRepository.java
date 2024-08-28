package org.boot.growup.product.persist.repository;

import org.boot.growup.product.persist.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}