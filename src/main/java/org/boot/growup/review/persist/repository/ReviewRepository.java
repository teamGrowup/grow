package org.boot.growup.review.persist.repository;

import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.review.persist.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Optional<Review> findById(Long reviewId);
    List<Review> findByOrderItemProductIn(List<Product> products); // OrderItem을 통해 Product 기준으로 리뷰 조회
}