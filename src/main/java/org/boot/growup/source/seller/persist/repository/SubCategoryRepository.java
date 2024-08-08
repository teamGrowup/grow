package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubCategoryRepository extends JpaRepository<SubCategory, Long> {
    // 추가적인 쿼리 메서드를 필요에 따라 정의할 수 있습니다.
}