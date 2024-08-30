package org.boot.growup.product.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.service.SellerService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.common.constant.Section;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.product.dto.response.*;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.product.persist.entity.ProductImage;
import org.boot.growup.product.persist.entity.ProductOption;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductApplication {

    private final ProductService productService;
    private final SellerService sellerService;
    private final CustomerService customerService;

    @Transactional
    public void postProductWithImages(PostProductRequestDTO postProductRequestDto, List<MultipartFile> productImages) {
        Seller seller = sellerService.getCurrentSeller();

        Section section = Section.PRODUCT_IMAGE; // 적절한 섹션으로 변경
        Product product = productService.postProduct(postProductRequestDto, seller);

        productService.postProductImages(productImages, product, section);

    }

    public GetSellerProductsResponseDTO getSellerProducts() {
        Seller seller = sellerService.getCurrentSeller(); // 현재 판매자 정보 가져오기

        List<Product> products = productService.getProductsBySellerId(seller.getId()); // 판매자의 모든 상품 목록 조회

        List<GetSellerProductResponseDTO> productResponses = products.stream()
                .map(product -> {
                    List<ProductImage> productImages = productService.getProductImages(product.getId());
                    List<ProductOption> productOptions = productService.getProductOptions(product.getId()); // 상품 옵션 조회

                    return GetSellerProductResponseDTO.builder()
                            .name(product.getName())
                            .description(product.getDescription())
                            .productImages(
                                    productImages.stream()
                                            .map(GetSellerProductResponseDTO.ProductImageDTO::from)
                                            .toList()
                            )
                            .productOption(
                                    productOptions.stream()
                                            .map(GetProductDetailResponseDTO.ProductOptionDTO::from)
                                            .toList()
                            ) // 상품 옵션 추가
                            .build();
                }).toList();

        return GetSellerProductsResponseDTO.builder()
                .products(productResponses)
                .build(); // 여러 상품에 대한 응답 DTO
    }



    public GetProductDetailResponseDTO getProductDetail(Long productId) {
        Product product = productService.getProductById(productId)
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
            productService.patchProductImages(productImages, product, section);
        } else {
            System.out.println("업데이트할 상품 이미지가 없습니다. 기존 이미지를 유지합니다.");
        }
    }

    public void deleteProduct(Long productId) {
        productService.deleteProductById(productId);
    }

    public void denyProduct(Long productId) {
        // 상품 상태를 DENIED로 변경
        productService.changeProductAuthority(productId, AuthorityStatus.DENIED);
    }

    public void approveProduct(Long productId) {
        // 상품 상태를 APPROVED로 변경
        productService.changeProductAuthority(productId, AuthorityStatus.APPROVED);
    }

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

    /*
    상품 좋아요 증가
     */
    public void postProductLike(Long productId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        productService.postProductLike(productId, customer);
    }

    /*
    상품 좋아요 취소
     */
    public void deleteProductLike(Long productId) {
        Customer customer = customerService.getCurrentCustomer(); // 현재 고객 정보 가져오기
        productService.deleteProductLike(productId, customer);
    }

    public SellerSalesResponseDTO getSalesBySellerId(Long sellerId) {
        return productService.getSalesBySellerId(sellerId);
    }
}
