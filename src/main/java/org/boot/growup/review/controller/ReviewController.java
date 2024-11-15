package org.boot.growup.review.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.review.application.ReviewApplication;
import org.boot.growup.review.dto.request.PostReviewRequestDTO;
import org.boot.growup.review.dto.response.GetBrandReviewResponseDTO;
import org.boot.growup.review.dto.response.GetReviewResponseDTO;
import org.boot.growup.review.persist.entity.Review;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class ReviewController {
    private final ReviewApplication reviewApplication;

    /**
     * [POST]
     * 리뷰 등록 요청
     * @header Customer's AccessToken
     * @body PostReviewRequestDTO, MultipartFile[]
     * @response Review
     */
    @PostMapping("/customers/reviews")
    public BaseResponse<String> postReviewWithImages(
            @RequestPart(value = "form") PostReviewRequestDTO requestDTO,
            @RequestPart(value = "images", required = false) List<MultipartFile> reviewImages
    ) {
        Review review = reviewApplication.postReviewWithImages(requestDTO, reviewImages);
        return new BaseResponse<>("리뷰 등록에 성공하셨습니다.");
    }

    /**
     * [GET]
     * 특정 리뷰 조회
     * @header Customer's AccessToken
     * @param reviewId 리뷰 ID
     * @response Optional<Review>
     */
    @GetMapping("/customers/reviews/{reviewId}")
    public BaseResponse<Optional<GetReviewResponseDTO>> getReviewById(@PathVariable Long reviewId) {
        Optional<GetReviewResponseDTO> review = reviewApplication.getReviewById(reviewId);
        return new BaseResponse<>(review);
    }

    /**
     * [GET]
     * 모든 리뷰 조회
     * @header Admin's AccessToken
     * @response BaseResponse<List<ReviewResponseDTO>>
     */
    @GetMapping("/admins/reviews")
    public BaseResponse<List<GetReviewResponseDTO>> getAllReviews() {
        List<GetReviewResponseDTO> reviews = reviewApplication.getAllReviews();
        return new BaseResponse<>(reviews);
    }

    /**
     * [GET]
     * 특정 브랜드의 모든 리뷰 조회
     * @header Customer's AccessToken
     * @param brandId 브랜드 ID
     * @response BaseResponse<BrandReviewsResponseDTO>
     */
    @GetMapping("/customers/reviews/brands/{brandId}")
    public BaseResponse<GetBrandReviewResponseDTO> getReviewsByBrandId(@PathVariable Long brandId) {
        GetBrandReviewResponseDTO response = reviewApplication.getReviewsByBrandId(brandId);
        return new BaseResponse<>(response);
    }

    /**
     * [PATCH]
     * 구매자의 리뷰 수정
     * @header Customer's AccessToken
     * @param reviewId 리뷰 ID
     * @body PostReviewRequestDTO
     * @response String
     */
    @PatchMapping("/customers/reviews/{reviewId}")
    public BaseResponse<String> patchReview(
            @PathVariable Long reviewId,
            @RequestPart(value = "images", required = false) List<MultipartFile> reviewImages,
            @RequestPart(value = "form") PostReviewRequestDTO reviewRequestDTO
    ) {
        reviewApplication.patchReview(reviewRequestDTO, reviewImages, reviewId);
        return new BaseResponse<>("리뷰 수정이 완료되었습니다.");
    }

    /**
     * [DELETE]
     * 구매자의 리뷰 삭제
     * @header Customer's AccessToken
     * @param reviewId 리뷰 ID
     * @response String
     */
    @DeleteMapping("/customers/reviews/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable Long reviewId) {
        reviewApplication.deleteReview(reviewId);
        return new BaseResponse<>("리뷰 삭제가 완료되었습니다.");
    }

    /**
     * [DELETE]
     * 관리자의 리뷰 삭제
     * @header Admin's AccessToken
     * @param reviewId 리뷰 ID
     * @response String
     */
    @DeleteMapping("/admins/reviews/{reviewId}")
    public BaseResponse<String> adminDeleteReview(@PathVariable Long reviewId) {
        reviewApplication.deleteReview(reviewId);
        return new BaseResponse<>("리뷰 삭제가 완료되었습니다.");
    }

    /**
     * [POST]
     * 리뷰에 좋아요 추가 요청
     * @header Customer's AccessToken
     * @param reviewId 리뷰 ID
     * @response String
     */
    @PostMapping("/customers/reviews/{reviewId}/like")
    public BaseResponse<String> postReviewLike(@PathVariable Long reviewId) {
        reviewApplication.postReviewLike(reviewId);
        return new BaseResponse<>("리뷰에 좋아요가 추가되었습니다.");
    }

    /**
     * [DELETE]
     * 리뷰 좋아요 삭제 요청
     * @header Customer's AccessToken
     * @param reviewId 리뷰 ID
     * @response String
     */
    @DeleteMapping("/customers/reviews/{reviewId}/like")
    public BaseResponse<String> deleteReviewLike(@PathVariable Long reviewId) {
        reviewApplication.deleteReviewLike(reviewId);
        return new BaseResponse<>("리뷰 좋아요가 삭제되었습니다.");
    }
}
