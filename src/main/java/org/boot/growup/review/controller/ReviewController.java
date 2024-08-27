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
@RequestMapping("/customers/reviews")
public class ReviewController {

    private final ReviewApplication reviewApplication;

    /**
     * [POST]
     * 리뷰 등록 요청
     * @header null
     * @body PostReviewRequestDTO, MultipartFile[]
     * @response Review
     */
    @PostMapping
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
     * @header null
     * @param reviewId 리뷰 ID
     * @response Optional<Review>
     */
    @GetMapping("/{reviewId}")
    public BaseResponse<Optional<GetReviewResponseDTO>> getReviewById(@PathVariable Long reviewId) {
        Optional<GetReviewResponseDTO> review = reviewApplication.getReviewById(reviewId);
        return new BaseResponse<>(review);
    }
    /**
     * [GET]
     * 모든 리뷰 조회
     * @response BaseResponse<List<ReviewResponseDTO>>
     */
    @GetMapping
    public BaseResponse<List<GetReviewResponseDTO>> getAllReviews() {
        List<GetReviewResponseDTO> reviews = reviewApplication.getAllReviews();
        return new BaseResponse<>(reviews);
    }

    /**
     * [GET]
     * 특정 브랜드의 모든 리뷰 조회
     * @param brandId 브랜드 ID
     * @response BaseResponse<BrandReviewsResponseDTO>
     */
    @GetMapping("/brands/{brandId}")
    public BaseResponse<GetBrandReviewResponseDTO> getReviewsByBrandId(@PathVariable Long brandId) {
        GetBrandReviewResponseDTO response = reviewApplication.getReviewsByBrandId(brandId);
        return new BaseResponse<>(response);
    }

    /**
     * [PATCH]
     * 리뷰 수정 요청
     * @header null
     * @param reviewId 리뷰 ID
     * @body PostReviewRequestDTO
     * @response String
     */
    @PatchMapping("/{reviewId}")
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
     * 리뷰 삭제 요청
     * @header null
     * @param reviewId 리뷰 ID
     * @response String
     */
    @DeleteMapping("/{reviewId}")
    public BaseResponse<String> deleteReview(@PathVariable Long reviewId) {
        reviewApplication.deleteReview(reviewId);
        return new BaseResponse<>("리뷰 삭제가 완료되었습니다.");
    }

    /**
     * [POST]
     * 리뷰에 좋아요 추가 요청
     * @header null
     * @param reviewId 리뷰 ID
     * @response String
     */
    @PostMapping("/{reviewId}/like")
    public BaseResponse<String> postReviewLike(@PathVariable Long reviewId) {
        reviewApplication.postReviewLike(reviewId);
        return new BaseResponse<>("리뷰에 좋아요가 추가되었습니다.");
    }

    /**
     * [DELETE]
     * 리뷰 좋아요 삭제 요청
     * @header null
     * @param reviewId 리뷰 ID
     * @response String
     */
    @DeleteMapping("/{reviewId}/like")
    public BaseResponse<String> deleteReviewLike(@PathVariable Long reviewId) {
        reviewApplication.deleteReviewLike(reviewId);
        return new BaseResponse<>("리뷰 좋아요가 삭제되었습니다.");
    }
}
