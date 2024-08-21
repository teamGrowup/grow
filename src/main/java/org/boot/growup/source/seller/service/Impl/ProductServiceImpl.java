package org.boot.growup.source.seller.service.Impl;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.boot.growup.source.seller.persist.repository.ProductLikeRepository;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.boot.growup.source.seller.service.ProductService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductLikeRepository productLikeRepository;

    @Override
    public Product postProduct(PostProductRequestDTO postProductRequestDto, Seller seller) {
        // 서브 카테고리 가져오기
        SubCategory subCategory = subCategoryRepository.findById(postProductRequestDto.getSubCategoryId())
                .orElseThrow(() -> new BaseException(ErrorCode.SUBCATEGORY_NOT_FOUND));
        // 브랜드 가져오기
        Brand brand = brandRepository.findById(postProductRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));
        Product product = Product.of(postProductRequestDto, brand, subCategory);

        // 상품 옵션 설정
        List<ProductOption> productOptions = convertToProductOptions(postProductRequestDto.getProductOptions(), product);
        product.initProductOptions(productOptions); // 상품 옵션 초기화 메서드 사용

        product.patchSubCategory(subCategory); // 서브 카테고리 설정
        product.patchBrand(brand);
        product.pending();
        product.initAverageRating();
        product.initLikeCount();
        product.designateSeller(seller); // 판매자 설정.

        productRepository.save(product);
        return product;
    }

    @Override
    public Product getProductBySellerId(Long sellerId) {
        return productRepository.findBySeller_Id(sellerId).orElseThrow(
                () -> new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND)
        );
    }

    // ProductRequestDTO의 ProductOptionDTO를 ProductOption으로 변환하는 메서드
    private List<ProductOption> convertToProductOptions(List<PostProductRequestDTO.ProductOptionDTO> productOptionDTOs, Product product) {
        return productOptionDTOs.stream()
                .map(dto -> ProductOption.builder()
                        .optionName(dto.getOptionName())
                        .optionStock(dto.getOptionStock())
                        .optionPrice(dto.getOptionPrice())
                        .product(product) // Product 설정 추가
                        .build())
                .toList();
    }

    @Override
    public Product patchProduct(PostProductRequestDTO postProductRequestDto, Seller seller, Long productId) {
        // 판매자ID와 상품 ID로 상품 조회
        Product product = productRepository.findByIdAndSeller_Id(productId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND));

        // Brand 조회
        Brand brand = brandRepository.findById(postProductRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));

        // 상태 변경 및 정보 업데이트
        product.pending(); // 대기 상태로 변경.
        product.patchProductInfo(postProductRequestDto.getName(), postProductRequestDto.getDescription());
        product.patchBrand(brand); // Brand 정보 업데이트

        // 상품 저장
        productRepository.save(product);
        return product;
    }

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
    public List<Product> getProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        if (authorityStatus == null) {
            return productRepository.findAll(pageable).stream().toList();
        }

        return productRepository.findByAuthorityStatus(authorityStatus, pageable);
    }

    public void postProductLike(Long productId, Customer customer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductLike productLike = ProductLike.builder()
                .customer(customer)
                .product(product)
                .build();

        product.likeCountPlus();

        productLikeRepository.save(productLike);
    }

    public void deleteProductLike(Long productId, Customer customer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        // 좋아요 정보 찾기
        ProductLike productLike = productLikeRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_LIKE_NOT_FOUND));

        // 좋아요 수 감소
        product.likeCountMinus();

        // 좋아요 정보 삭제
        productLikeRepository.delete(productLike);
    }
}
