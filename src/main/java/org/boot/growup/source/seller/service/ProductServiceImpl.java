package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage; // ProductImage 엔티티 임포트
import org.boot.growup.source.seller.persist.entity.ProductOption;
import org.boot.growup.source.seller.persist.entity.SubCategory;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository; // ProductImageRepository 임포트
import org.boot.growup.source.seller.ImageStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ImageStore imageStore; // ImageStore 주입

    @Override
    @Transactional
    public ProductResponseDTO registerProduct(ProductRequestDTO productRequestDto, List<MultipartFile> images) {
        SubCategory subCategory = subCategoryRepository.findById(productRequestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브 카테고리 ID입니다."));

        Product product = Product.builder()
                .productName(productRequestDto.getProductName())
                .productDescription(productRequestDto.getProductDescription())
                .subCategory(subCategory)
                .authorityStatus(AuthorityStatus.PENDING)
                .productOptions(mapToProductOptions(productRequestDto.getProductOptions()))
                .build();

        productRepository.save(product);

        if (images != null && !images.isEmpty()) {
            saveProductImages(images, product);
        }

        return new ProductResponseDTO("등록 성공", product.getId());
    }

    private void saveProductImages(List<MultipartFile> images, Product product) {
        for (MultipartFile image : images) {
            if (!image.isEmpty()) {
                ProductImage productImage = ProductImage.builder()
                        .originalImageName(image.getOriginalFilename())
                        .path(imageStore.createStoreFileName(image.getOriginalFilename())) // 저장된 이름
                        .product(product) // 상품과 연결
                        .build();

                // 이미지 저장 로직 추가
                imageStore.storeImage(image, productImage.getPath()); // 이미지 저장 메서드 호출

                product.getProductImages().add(productImage);
            }
        }
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
