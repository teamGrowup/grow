package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.source.seller.persist.entity.MainCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainCategoryRepository extends JpaRepository<MainCategory, Long> {
    // 추가적인 쿼리 메서드를 필요에 따라 정의할 수 있습니다.
}