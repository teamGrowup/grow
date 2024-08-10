package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.ImageStore;
import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO; // ProductRequestDTO 임포트
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO; // ProductDetailResponseDTO 임포트
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import jakarta.transaction.Transactional;

import java.util.List;

import static org.boot.growup.source.seller.persist.entity.QProduct.product;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final ProductImageRepository productImageRepository;
    private final SellerRepository sellerRepository;
    private final ImageStore imageStore;

    @Override
    @Transactional
    public ProductResponseDTO registerProduct(ProductRequestDTO productRequestDto, List<MultipartFile> images) {
        // 서브 카테고리 가져오기
        SubCategory subCategory = subCategoryRepository.findById(productRequestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브 카테고리 ID입니다."));

        // 판매자 가져오기
        Seller seller = sellerRepository.findById(productRequestDto.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 판매자 ID입니다."));

        // Product 객체 생성
        Product product = Product.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .subCategory(subCategory)
                .seller(seller)
                .authorityStatus(AuthorityStatus.PENDING)
                .build(); // 여기까지 product 객체 생성

        // 상품 옵션 설정
        List<ProductOption> productOptions = convertToProductOptions(productRequestDto.getProductOptions(), product);
        product.initProductOptions(productOptions); // 상품 옵션 초기화 메서드 사용

        // 상품 저장
        productRepository.save(product);

        // 상품 이미지 저장
        saveProductImages(product, productRequestDto.getProductImages());

        return new ProductResponseDTO("등록 성공", product.getId());
    }


    // ProductRequestDTO의 ProductOptionDTO를 ProductOption으로 변환하는 메서드
    private List<ProductOption> convertToProductOptions(List<ProductRequestDTO.ProductOptionDTO> productOptionDTOs, Product product) {
        return productOptionDTOs.stream()
                .map(dto -> ProductOption.builder()
                        .optionName(dto.getOptionName())
                        .optionStock(dto.getOptionStock())
                        .optionPrice(dto.getOptionPrice())
                        .product(product) // Product 설정 추가
                        .build())
                .toList();
    }

    // ProductImage 저장 메서드
    private void saveProductImages(Product product, List<ProductRequestDTO.ProductImageDTO> productImages) {
        List<ProductImage> images = productImages.stream()
                .map(dto -> ProductImage.builder()
                        .originalImageName(dto.getOriginalImageName())
                        .path(dto.getPath())
                        .product(product) // Product 설정 추가
                        .build())
                .toList();

        productImageRepository.saveAll(images); // 이미지 저장
    }


}
