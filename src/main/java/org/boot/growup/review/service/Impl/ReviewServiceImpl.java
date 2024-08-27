package org.boot.growup.review.service.Impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.persist.repository.CustomerRepository;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.utils.ImageStore;
import org.boot.growup.common.utils.S3Service;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.repository.ProductRepository;
import org.boot.growup.review.dto.request.PostReviewRequestDTO;
import org.boot.growup.review.dto.response.GetBrandReviewResponseDTO;
import org.boot.growup.review.dto.response.GetReviewResponseDTO;
import org.boot.growup.review.persist.entity.Review;
import org.boot.growup.review.persist.entity.ReviewImage;
import org.boot.growup.review.persist.entity.ReviewLike;
import org.boot.growup.review.persist.repository.ReviewRepository;
import org.boot.growup.review.persist.repository.ReviewImageRepository;
import org.boot.growup.review.persist.repository.ReviewLikeRepository;
import org.boot.growup.order.persist.entity.OrderItem;
import org.boot.growup.order.persist.repository.OrderItemRepository;
import org.boot.growup.review.service.ReviewService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewLikeRepository reviewLikeRepository;
    private final CustomerRepository customerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;
    private final S3Service s3Service;  // S3 서비스
    private final ImageStore imageStore; // 이미지 저장 서비스

    @Override
    public Review postReview(PostReviewRequestDTO requestDTO, Long customerId) {
        // 고객 조회
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BaseException(ErrorCode.CUSTOMER_NOT_FOUND));

        // 주문 항목 조회
        OrderItem orderItem = orderItemRepository.findById(requestDTO.getOrderItemId())
                .orElseThrow(() -> new BaseException(ErrorCode.ORDER_ITEM_NOT_FOUND));

        // Review 엔티티 생성
        Review review = Review.of(requestDTO, customer, orderItem);

        // 리뷰 저장
        reviewRepository.save(review);

        // 리뷰 엔티티 반환
        return review;
    }

    @Override
    public Optional<GetReviewResponseDTO> getReviewById(Long reviewId) {
        return reviewRepository.findById(reviewId).map(review -> {
            List<GetReviewResponseDTO.ReviewImageDTO> imageDTOs = review.getReviewImages().stream()
                    .map(GetReviewResponseDTO.ReviewImageDTO::from) // ReviewImage 엔티티를 ReviewImageDTO로 변환
                    .collect(Collectors.toList());

            return GetReviewResponseDTO.builder()
                    .reviewId(review.getId())
                    .author(review.getAuthor())
                    .content(review.getContent())
                    .rating(review.getRating())
                    .likeCount(review.getLikeCount())
                    .productId(review.getOrderItem().getProduct().getId())
                    .productName(review.getOrderItem().getProduct().getName())
                    .reviewImageDTO(imageDTOs)
                    .build();
        });
    }

    @Override
    public List<GetReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public GetBrandReviewResponseDTO getReviewsByBrandId(Long brandId) {
        // 브랜드에 속한 상품 조회
        List<Product> products = productRepository.findByBrandId(brandId);
        // 해당 상품에 대한 리뷰 조회
        List<Review> reviews = reviewRepository.findByOrderItemProductIn(products);

        // 리뷰를 DTO로 변환
        List<GetReviewResponseDTO> reviewDTOs = reviews.stream()
                .map(review -> GetReviewResponseDTO.builder()
                        .reviewId(review.getId())
                        .author(review.getAuthor())
                        .content(review.getContent())
                        .rating(review.getRating())
                        .likeCount(review.getLikeCount())
                        .productId(review.getOrderItem().getProduct().getId())
                        .productName(review.getOrderItem().getProduct().getName())
                        .build())
                .collect(Collectors.toList());

        return new GetBrandReviewResponseDTO(brandId, reviewDTOs);
    }

    private GetReviewResponseDTO convertToDTO(Review review) {
        List<GetReviewResponseDTO.ReviewImageDTO> imageDTOs = review.getReviewImages().stream()
                .map(GetReviewResponseDTO.ReviewImageDTO::from)
                .collect(Collectors.toList());

        return GetReviewResponseDTO.builder()
                .reviewId(review.getId())
                .author(review.getAuthor())
                .content(review.getContent())
                .rating(review.getRating())
                .likeCount(review.getLikeCount())
                .productId(review.getOrderItem().getProduct().getId())
                .productName(review.getOrderItem().getProduct().getName())
                .reviewImageDTO(imageDTOs)
                .build();
    }

    @Override
    public Review patchReview(PostReviewRequestDTO requestDTO, Customer customer, Long reviewId) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        // 리뷰가 해당 고객의 것인지 확인
        if (!review.getCustomer().getId().equals(customer.getId())) {
            throw new BaseException(ErrorCode.NO_PERMISSION_TO_MODIFY_REVIEW);
        }

        // 리뷰의 내용 업데이트
        review.patchReviewInfo(requestDTO.getContent(), requestDTO.getRating());

        // 리뷰 저장
        reviewRepository.save(review);

        // 수정된 리뷰 엔티티 반환
        return review;
    }

    @Override
    public void deleteReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        // 관련된 리뷰 이미지를 삭제
        review.getReviewImages().clear(); // 이 라인으로 관련된 이미지들을 삭제
        reviewRepository.deleteById(reviewId);
    }

    @Override
    public void postReviewImages(List<MultipartFile> reviewImages, Review review) {
        for (MultipartFile multipartFile : reviewImages) {
            ReviewImage uploadImage = storeImage(multipartFile);
            uploadImage.designateReview(review);
            reviewImageRepository.save(uploadImage);
        }
    }

    @Override
    @Transactional
    public void patchReviewImages(List<MultipartFile> reviewImages, Review review) {
        // 1. 현재 S3에 등록된 리뷰 이미지를 지움.
        reviewImageRepository.findReviewImageByReview_Id(review.getId()).forEach(m -> s3Service.deleteFile(m.getPath()));

        // 2. DB에 있는 리뷰 이미지 삭제.
        reviewImageRepository.deleteReviewImageByReview_Id(review.getId());

        // 3. 해당 리뷰에 이미지를 새로 등록함.
        for (MultipartFile multipartFile : reviewImages) {
            ReviewImage uploadImage = storeImage(multipartFile);
            uploadImage.designateReview(review);
            reviewImageRepository.save(uploadImage);
        }
    }

    private ReviewImage storeImage(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("이미지가 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 원래 이름
        String storeFilename = imageStore.createStoreFileName(originalFilename); // 저장된 이름
        String path;

        try {
            path = s3Service.uploadFileAndGetUrl(multipartFile, storeFilename);
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }

        return ReviewImage.builder()
                .originalImageName(originalFilename)
                .path(path)
                .build();
    }

    @Override
    public void postReviewLike(Long reviewId, Customer customer) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        // 이미 좋아요를 누른 경우
        if (reviewLikeRepository.existsByCustomerIdAndReviewId(customer.getId(), reviewId)) {
            throw new BaseException(ErrorCode.ALREADY_LIKED_REVIEW);
        }

        // 리뷰 좋아요 생성
        ReviewLike reviewLike = ReviewLike.builder()
                .customer(customer)
                .review(review)
                .build();

        // 리뷰 좋아요 수 증가
        review.likeCountPlus();

        // 데이터베이스에 저장
        reviewLikeRepository.save(reviewLike);
        reviewRepository.save(review); // 변경된 리뷰 저장
    }

    @Override
    public void deleteReviewLike(Long reviewId, Customer customer) {
        // 리뷰 조회
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new BaseException(ErrorCode.REVIEW_NOT_FOUND));

        // 구매자ID와 리뷰ID로 정보 찾기
        ReviewLike reviewLike = reviewLikeRepository.findByCustomerIdAndReviewId(customer.getId(), review.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_LIKE_NOT_FOUND));

        // 리뷰 좋아요 수 감소
        review.likeCountMinus();

        // 데이터베이스에서 삭제
        reviewLikeRepository.delete(reviewLike);
    }
}