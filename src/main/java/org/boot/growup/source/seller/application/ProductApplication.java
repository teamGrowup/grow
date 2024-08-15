package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetProductRequestByStatusResponseDTO;
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

    /*
    상품 등록 및 이미지 저장
     */
    @Transactional
    public void postProductWithImages(PostProductRequestDTO postProductRequestDto, List<MultipartFile> productImages) {
        // 현재 유저가 seller인지 확인 및 seller 가져오기 (여기서는 하드코딩된 ID 사용)
        Long sellerId = 1L; // 예시: 실제 판매자 ID를 가져오는 로직을 작성해야 함.

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()->new BaseException(ErrorCode.SELLER_NOT_FOUND));

        // 상품 등록 요청 DTO에 판매자 ID 설정
        postProductRequestDto.setSellerId(seller.getId());

        Section section = Section.PRODUCT_IMAGE; // 적절한 섹션으로 변경
        Product product = productService.registerProduct(postProductRequestDto, seller);

        productImageService.saveProductImages(productImages, product, section);

    }


    public ProductDetailResponseDTO getProductDetail(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        return ProductDetailResponseDTO.from(product);
    }

    @Transactional
    public void patchProduct(PostProductRequestDTO postProductRequestDto, List<MultipartFile> productImages, Long productId) {

        Seller seller = sellerRepository.findById(postProductRequestDto.getSellerId())
                .orElseThrow(() -> new BaseException(ErrorCode.SELLER_NOT_FOUND));

        // 이미지 처리
        Section section = Section.PRODUCT_IMAGE;
        Product product = productService.patchProduct(postProductRequestDto, seller, productId);

        if (productImages != null && !productImages.isEmpty()) {
            productImageService.patchProductImages(productImages, product, section);
        } else {
            System.out.println("업데이트할 상품 이미지가 없습니다. 기존 이미지를 유지합니다.");
        }
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
        List<Product> productList = productService.readProductRequestsByStatus(authorityStatus, pageNo);
        return productList.stream()
                .map(GetProductRequestByStatusResponseDTO::from)
                .toList();
    }
}
