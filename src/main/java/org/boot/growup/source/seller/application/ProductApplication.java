package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.service.ProductImageServiceImpl;
import org.boot.growup.source.seller.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductApplication {

    private final ProductService productService;
    private final ProductImageServiceImpl productImageService;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    /**
     * 상품 등록 및 이미지 저장
     */
    @Transactional
    public void registerProductWithImages(ProductRequestDTO productRequestDto, List<MultipartFile> productImages) {
        // 현재 유저가 seller인지 확인 및 seller 가져오기 (여기서는 하드코딩된 ID 사용)
        Long sellerId = 1L; // 예시: 실제 판매자 ID를 가져오는 로직을 작성해야 함.

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()->new BaseException(ErrorCode.SELLER_NOT_FOUND));

        // 상품 등록 요청 DTO에 판매자 ID 설정
        productRequestDto.setSellerId(seller.getId());

        Section section = Section.PRODUCT_IMAGE; // 적절한 섹션으로 변경
        Product product = productService.registerProduct(productRequestDto, seller);
        // 이미지 저장
        productImageService.saveProductImages(productImages, product, section);

    }

    /**
     * 상품 ID를 통해 상품의 상세 정보를 확인합니다.
     */
    public ProductDetailResponseDTO getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        // 상품의 상세 정보를 DTO로 변환하여 반환
        return ProductDetailResponseDTO.builder()
                .productId(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .averageRating(product.getAverageRating())
                .likeCount(product.getLikeCount())
                .authorityStatus(product.getAuthorityStatus())
                .subCategoryId(product.getSubCategory().getId()) // 서브 카테고리 ID 가져오기
                .mainCategoryId(product.getSubCategory().getMainCategory().getId()) // 메인 카테고리 ID 가져오기
                .productImages(convertToProductImageDTOs(product.getProductImages()))
                .productOptions(convertToProductOptionDTOs(product.getProductOptions()))
                .build();
    }

    // 상품 이미지 리스트를 DTO로 변환하는 메서드
    private List<ProductDetailResponseDTO.ProductImageDTO> convertToProductImageDTOs(List<ProductImage> productImages) {
        return productImages.stream()
                .map(ProductDetailResponseDTO.ProductImageDTO::from) // from 메서드 호출
                .toList();
    }

    // 상품 옵션 리스트를 DTO로 변환하는 메서드
    private List<ProductDetailResponseDTO.ProductOptionDTO> convertToProductOptionDTOs(List<ProductOption> productOptions) {
        return productOptions.stream()
                .map(option -> ProductDetailResponseDTO.ProductOptionDTO.builder()
                        .optionName(option.getOptionName())
                        .optionStock(option.getOptionStock())
                        .optionPrice(option.getOptionPrice())
                        .build())
                .toList();
    }
    @Transactional
    public void updateProduct(ProductRequestDTO productRequestDto, List<MultipartFile> productImages) {

        Long sellerId = 1L; // 실제 seller ID로 변경
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()->new BaseException(ErrorCode.SELLER_NOT_FOUND));
        Section section = Section.PRODUCT_IMAGE;

        Product product = productService.updateProduct(productRequestDto, seller);

        if (productImages != null && !productImages.isEmpty()) {
            productImageService.updateProductImages(productImages, product, section);
        } else {
            System.out.println("업데이트할 상품 이미지가 없습니다. 기존 이미지를 유지합니다.");
            // 기존 이미지를 유지하는 로직 추가 가능
        }
    }


}
