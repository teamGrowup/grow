package org.boot.growup.source.seller.service;

import org.boot.growup.source.seller.dto.ProductRequestDTO;
import org.boot.growup.source.seller.dto.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage; // ProductImage 엔티티 임포트
import org.boot.growup.source.seller.persist.entity.ProductOption;
import org.boot.growup.source.seller.persist.entity.SubCategory;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository; // ProductImageRepository 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Autowired
    private final ProductRepository productRepository;

    @Autowired
    private final SubCategoryRepository subCategoryRepository;

    @Autowired
    private final ProductImageRepository productImageRepository; // ProductImageRepository 추가

    @Transactional
    public ProductResponseDTO registerProduct(ProductRequestDTO productRequestDto, List<MultipartFile> images) {

        SubCategory subCategory = subCategoryRepository.findById(productRequestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브 카테고리 ID입니다."));

        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .productDescription(productRequestDto.getProductDescription())
                .subCategory(subCategory)
                .authorityStatus("대기")
                .productOptions(mapToProductOptions(productRequestDto.getProductOptions()))
                .build();

        productRepository.save(product);

        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    ProductImage productImage = ProductImage.builder()
                            .originalImageName(image.getOriginalFilename())
                            .path("your/image/path/" + image.getOriginalFilename()) // 실제 이미지 저장 경로
                            .createdAt(LocalDateTime.now())
                            .modifiedAt(LocalDateTime.now())
                            .status("ACTIVE") // 상태 설정
                            .product(product) // 상품과 연결
                            .build();
                    product.getProductImages().add(productImage);
                }
            }
        }

        productRepository.save(product);

        return new ProductResponseDTO("등록 성공", product.getProductId());
    }

    private List<ProductOption> mapToProductOptions(List<ProductRequestDTO.ProductOptionDto> optionDtos) {
        return optionDtos.stream()
                .map(optionDto -> ProductOption.builder()
                        .productOptionName(optionDto.getProductOptionName())
                        .productOptionStock(optionDto.getProductOptionStock())
                        .productOptionPrice(optionDto.getProductOptionPrice())
                        .build())
                .toList();
    }
}
