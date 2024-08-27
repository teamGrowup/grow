package org.boot.growup.review.persist.repository;

import org.boot.growup.review.persist.entity.ReviewLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewLikeRepository extends JpaRepository<ReviewLike, Long> {
    // 고객 ID와 리뷰 ID로 좋아요 존재 여부 확인
    boolean existsByCustomerIdAndReviewId(Long customerId, Long reviewId);

    // 고객과 리뷰에 대한 좋아요 정보를 찾는 메서드 (필요시 추가)
    Optional<ReviewLike> findByCustomerIdAndReviewId(Long customerId, Long reviewId);
}