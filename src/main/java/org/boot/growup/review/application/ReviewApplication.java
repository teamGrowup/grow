package org.boot.growup.review.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.review.dto.request.PostReviewRequestDTO;
import org.boot.growup.review.dto.response.GetBrandReviewResponseDTO;
import org.boot.growup.review.dto.response.GetReviewResponseDTO;
import org.boot.growup.review.persist.entity.Review;
import org.boot.growup.review.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewApplication {
    private final ReviewService reviewService;
    private final CustomerService customerService; // 고객 서비스

    public Review postReviewWithImages(PostReviewRequestDTO requestDTO, List<MultipartFile> reviewImages) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기

        // 리뷰 생성
        Review review = reviewService.postReview(requestDTO, customer.getId());

        // 리뷰에 이미지 추가
        reviewService.postReviewImages(reviewImages, review);

        return review; // 생성된 리뷰 반환
    }

    public Optional<GetReviewResponseDTO> getReviewById(Long reviewId) {
        return reviewService.getReviewById(reviewId); // 리뷰 조회
    }
    public List<GetReviewResponseDTO> getAllReviews(){
        return reviewService.getAllReviews();
    }

    public GetBrandReviewResponseDTO getReviewsByBrandId(Long brandId) {
        return reviewService.getReviewsByBrandId(brandId);
    }

    public void patchReview(PostReviewRequestDTO requestDTO,List<MultipartFile> reviewImages, Long reviewId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        Review review = reviewService.patchReview(requestDTO, customer, reviewId); // 리뷰 수정

        if (reviewImages != null && !reviewImages.isEmpty()) {
            reviewService.patchReviewImages(reviewImages, review);
        } else {
            // 이미지가 없는 경우
            throw new BaseException(ErrorCode.NO_IMAGES_TO_UPDATE);
        }
    }

    public void deleteReview(Long reviewId) {
        reviewService.deleteReviewById(reviewId); // 리뷰 삭제
    }

    public void postReviewLike(Long reviewId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        reviewService.postReviewLike(reviewId, customer); // 리뷰에 좋아요 추가
    }

    public void deleteReviewLike(Long reviewId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        reviewService.deleteReviewLike(reviewId, customer); // 리뷰의 좋아요 삭제
    }
}
