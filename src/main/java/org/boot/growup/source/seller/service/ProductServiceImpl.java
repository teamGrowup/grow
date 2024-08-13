package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO; // ProductRequestDTO 임포트
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

import static org.boot.growup.source.seller.persist.entity.QProduct.product;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;

    @Override
    @Transactional
    public Product registerProduct(ProductRequestDTO productRequestDto, Seller seller) {
        // 서브 카테고리 가져오기
        SubCategory subCategory = subCategoryRepository.findById(productRequestDto.getSubCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 서브 카테고리 ID입니다."));

        Product product = Product.from(productRequestDto);

        // 상품 옵션 설정
        List<ProductOption> productOptions = convertToProductOptions(productRequestDto.getProductOptions(), product);
        product.initProductOptions(productOptions); // 상품 옵션 초기화 메서드 사용

        product.setSubCategory(subCategory); // 서브 카테고리 설정
        product.pending();
        product.initAverageRating();
        product.initLikeCount();
        product.designateSeller(seller); // 판매자 설정.

        productRepository.save(product);
        return product;
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
    @Transactional
    @Override
    public Product updateProduct(ProductRequestDTO productRequestDto, Seller seller) {
        Product product = productRepository.findBySeller_Id(seller.getId()).orElseThrow(
                () -> new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND)
        );

        product.pending(); // 대기 상태로 변경.
        product.updateProductInfo(productRequestDto.getName(), productRequestDto.getDescription());

        productRepository.save(product);
        return product;
    }

}
