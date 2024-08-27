package org.boot.growup.review.persist.repository;

import org.boot.growup.review.persist.entity.ReviewImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImageRepository extends JpaRepository<ReviewImage, Long> {
    List<ReviewImage> findReviewImageByReview_Id(Long id);
    void deleteReviewImageByReview_Id(Long id);
}