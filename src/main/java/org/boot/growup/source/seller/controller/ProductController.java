package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.application.ProductApplication;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.dto.response.GetProductDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetProductRequestByStatusResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplication productApplication;

    /**
     * 현재 판매자의 제품 등록 요청
     * @param productImages
     * @param postProductRequestDto
     * @return BaseResponse<String>
     */
    @PostMapping("/sellers/products")
    public BaseResponse<String> postProduct(
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") PostProductRequestDTO postProductRequestDto
    ) {
        productApplication.postProductWithImages(postProductRequestDto, productImages);
        return new BaseResponse<>("등록 성공");
    }

    /**
     * 상품ID를 통해 상품의 상세 정보를 확인합니다.
     * @param productId 상품 ID
     * @return BaseResponse<ProductDetailResponseDTO>
     */
    @GetMapping("/sellers/products/{productId}")
    public BaseResponse<GetProductDetailResponseDTO> getProductDetail(@PathVariable Long productId) {
        GetProductDetailResponseDTO productDetail = productApplication.getProductDetail(productId);
        return new BaseResponse<>(productDetail);
    }

    /**
     * 현재 판매자의 제품 정보를 수정 요청
     * @param productId 상품 ID
     * @param productImages 수정할 이미지 파일 리스트
     * @param postProductRequestDto 수정할 제품 정보 DTO
     * @return BaseResponse<String>
     */
    @PatchMapping("/sellers/products/{productId}")
    public BaseResponse<String> patchProduct(
            @PathVariable Long productId,
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") PostProductRequestDTO postProductRequestDto
    ) {
        productApplication.patchProduct(postProductRequestDto, productImages, productId );
        return new BaseResponse<>("수정 성공");
    }

    /**
     * 관리자가 등록된 상품들의 승인 상태 '거부'로 변경.
     * @param productId
     * @return
     */
    @PatchMapping("/admins/product-requests/{productId}/deny")
    public BaseResponse<String> denyProductRegister(@PathVariable Long productId) {
        productApplication.denyProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 거부됐습니다.");
    }

    /**
     * 관리자가 등록된 상품들의 승인 상태 '승인'으로 변경.
     * @param productId
     * @return
     */
    @PatchMapping("/admins/product-requests/{productId}/approve")
    public BaseResponse<String> approveProductRegister(@PathVariable Long productId) {
        productApplication.approveProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 승인됐습니다.");
    }

    /**
     * 관리자가 등록된 상품들의 승인 상태 '대기'로 변경.
     * @param productId
     * @return
     */
    @PatchMapping("/admins/product-requests/{productId}/pending")
    public BaseResponse<String> pendingProductRegister(@PathVariable Long productId) {
        productApplication.pendingProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 대기 상태로 변경됐습니다.");
    }

    /**
     * 관리자가 등록된 상품들의 요청들을 상태별(AuthorityStatus) 페이징 조회할 수 있음.
     * @param authorityStatus
     * @param pageNo
     * @return
     */
    @GetMapping("/admins/product-requests")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getProductRequestsByStatus(
            @RequestParam(value = "authorityStatus", required = false) AuthorityStatus authorityStatus,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(authorityStatus, pageNo));
    }
}
