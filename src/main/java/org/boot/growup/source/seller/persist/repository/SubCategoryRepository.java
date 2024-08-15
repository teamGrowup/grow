package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
}