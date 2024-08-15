package org.boot.growup.source.seller.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;

    @Override
    @Transactional
    public Product registerProduct(ProductRequestDTO productRequestDto, Seller seller) {
        // 서브 카테고리 가져오기
        SubCategory subCategory = subCategoryRepository.findById(productRequestDto.getSubCategoryId())
                .orElseThrow(() -> new BaseException(ErrorCode.SUBCATEGORY_NOT_FOUND));
        // 브랜드 가져오기
        Brand brand = brandRepository.findById(productRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));
        Product product = Product.of(productRequestDto, brand, subCategory);

        // 상품 옵션 설정
        List<ProductOption> productOptions = convertToProductOptions(productRequestDto.getProductOptions(), product);
        product.initProductOptions(productOptions); // 상품 옵션 초기화 메서드 사용

        product.setSubCategory(subCategory); // 서브 카테고리 설정
        product.setBrand(brand);
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
    public Product updateProduct(ProductRequestDTO productRequestDto, Seller seller, Long productId) {
        // 판매자ID와 상품 ID로 상품 조회
        Product product = productRepository.findByIdAndSeller_Id(productId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND));

        // Brand 조회
        Brand brand = brandRepository.findById(productRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));

        // 상태 변경 및 정보 업데이트
        product.pending(); // 대기 상태로 변경.
        product.updateProductInfo(productRequestDto.getName(), productRequestDto.getDescription());
        product.setBrand(brand); // Brand 정보 업데이트

        // 상품 저장
        productRepository.save(product);
        return product;
    }

    @Transactional
    @Override
    public void changeProductAuthority(Long productId, AuthorityStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        switch (status) {
            case DENIED -> product.deny();
            case PENDING -> product.pending();
            case APPROVED -> product.approve();
        }

    }

    @Override
    public List<Product> readProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        if (authorityStatus == null) {
            return productRepository.findAll(pageable).stream().toList();
        }

        return productRepository.findByAuthorityStatus(authorityStatus, pageable);
    }

}
