package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
}