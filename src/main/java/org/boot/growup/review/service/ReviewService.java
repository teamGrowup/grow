package org.boot.growup.review.service;

import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.review.dto.request.PostReviewRequestDTO;
import org.boot.growup.review.dto.response.GetBrandReviewResponseDTO;
import org.boot.growup.review.dto.response.GetReviewResponseDTO;
import org.boot.growup.review.persist.entity.Review;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ReviewService {
    /*
    고객 ID와 리뷰 요청 DTO를 기반으로 새로운 리뷰를 생성하고 저장합니다.
     */
    Review postReview(PostReviewRequestDTO requestDTO, Long customerId);

    /*
    리뷰 ID를 사용하여 리뷰 정보를 가져옵니다.
     */
    Optional<GetReviewResponseDTO> getReviewById(Long reviewId);

    /*
    전체 리뷰 정보를 가져옵니다.
     */
    List<GetReviewResponseDTO> getAllReviews();

    /*
    브랜드 ID를 사용하여 리뷰 정보를 가져옵니다.
     */
    GetBrandReviewResponseDTO getReviewsByBrandId(Long brandId);

    /*
    리뷰 ID와 고객 정보를 기반으로 해당 리뷰를 수정합니다.
     */
    Review patchReview(PostReviewRequestDTO requestDTO, Customer customer, Long reviewId);

    /*
    리뷰 ID를 기반으로 특정 리뷰를 삭제합니다.
     */
    void deleteReviewById(Long reviewId);

    /*
    리뷰에 이미지를 추가합니다. MultipartFile 리스트와 리뷰 객체를 사용합니다.
     */
    void postReviewImages(List<MultipartFile> reviewImages, Review review);

    /*
    기존 리뷰 이미지를 삭제하고, 새로운 이미지를 등록합니다.
     */
    void patchReviewImages(List<MultipartFile> reviewImages, Review review);

    /*
    리뷰 ID와 고객 정보를 기반으로 리뷰에 좋아요를 추가합니다.
     */
    void postReviewLike(Long reviewId, Customer customer);

    /*
    리뷰 ID와 고객 정보를 기반으로 리뷰의 좋아요를 삭제합니다.
     */
    void deleteReviewLike(Long reviewId, Customer customer);
}
