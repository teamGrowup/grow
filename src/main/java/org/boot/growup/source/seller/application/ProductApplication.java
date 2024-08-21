package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.service.CustomerService;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.dto.response.GetSellerProductResponseDTO;
import org.boot.growup.source.seller.dto.response.GetProductDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetProductRequestByStatusResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.ProductLikeRepository;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.service.ProductImageService;
import org.boot.growup.source.seller.service.ProductService;
import org.boot.growup.source.seller.service.SellerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductApplication {

    private final ProductService productService;
    private final ProductImageService productImageService;
    private final SellerService sellerService;
    private final CustomerService customerService;
    private final ProductRepository productRepository;
    private final ProductLikeRepository productLikeRepository;

    @Transactional
    public void postProductWithImages(PostProductRequestDTO postProductRequestDto, List<MultipartFile> productImages) {
        Seller seller = sellerService.getCurrentSeller();

        Section section = Section.PRODUCT_IMAGE; // 적절한 섹션으로 변경
        Product product = productService.postProduct(postProductRequestDto, seller);

        productImageService.postProductImages(productImages, product, section);

    }

    /**
     * 현재 판매자의 상품 목록을 조회
     *
     * @return 판매자의 상품 목록
     */
    public GetSellerProductResponseDTO getSellerProduct() {
        Seller seller = sellerService.getCurrentSeller(); // 현재 판매자 정보 가져오기

        Product product = productService.getProductBySellerId(seller.getId()); // 판매자의 상품 목록 조회
        List<ProductImage> productImages = productImageService.getProductImages(product.getId());
        
        return GetSellerProductResponseDTO.builder()
                .name(product.getName())
                .description(product.getDescription())
                .productImages(
                        productImages.stream().map(GetSellerProductResponseDTO.ProductImageDTO::from).toList()
                )
                .build();
    }

    public GetProductDetailResponseDTO getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        return GetProductDetailResponseDTO.from(product);
    }

    @Transactional
    public void patchProduct(PostProductRequestDTO postProductRequestDto, List<MultipartFile> productImages, Long productId) {
        Seller seller = sellerService.getCurrentSeller();

        // 이미지 처리
        Section section = Section.PRODUCT_IMAGE;
        Product product = productService.patchProduct(postProductRequestDto, seller, productId);

        if (productImages != null && !productImages.isEmpty()) {
            productImageService.patchProductImages(productImages, product, section);
        } else {
            System.out.println("업데이트할 상품 이미지가 없습니다. 기존 이미지를 유지합니다.");
        }
    }

    /**
     * 상품 삭제
     * @param productId 상품 ID
     */
    @Transactional
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Transactional
    public void denyProduct(Long productId) {
        // 상품 상태를 DENIED로 변경
        productService.changeProductAuthority(productId, AuthorityStatus.DENIED);
    }

    @Transactional
    public void approveProduct(Long productId) {
        // 상품 상태를 APPROVED로 변경
        productService.changeProductAuthority(productId, AuthorityStatus.APPROVED);
    }

    @Transactional
    public void pendingProduct(Long productId) {
        // 상품 상태를 PENDING으로 변경
        productService.changeProductAuthority(productId, AuthorityStatus.PENDING);
    }

    public List<GetProductRequestByStatusResponseDTO> getProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        List<Product> productList = productService.getProductRequestsByStatus(authorityStatus, pageNo);
        return productList.stream()
                .map(GetProductRequestByStatusResponseDTO::from)
                .toList();
    }

    /**
     * 상품 좋아요 증가
     * @param productId 상품 ID
     */
    @Transactional
    public void likeProduct(Long productId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        // 좋아요 엔티티 생성
        ProductLike productLike = ProductLike.builder()
                .customer(customer)
                .product(product)
                .build();

        // 좋아요 수 증가
        product.likeCountPlus();

        // 좋아요 정보 저장
        productLikeRepository.save(productLike);
    }

    /**
     * 상품 좋아요 감소
     * @param productId 상품 ID
     */
    @Transactional
    public void unlikeProduct(Long productId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        // 좋아요 정보 찾기
        ProductLike productLike = productLikeRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new BaseException(ErrorCode.LIKE_NOT_FOUND));

        // 좋아요 수 감소
        product.likeCountMinus();

        // 좋아요 정보 삭제
        productLikeRepository.delete(productLike);
    }
}
