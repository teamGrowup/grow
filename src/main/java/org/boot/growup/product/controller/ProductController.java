package org.boot.growup.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.application.ProductApplication;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.dto.response.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplication productApplication;

    /**
     * [POST]
     * 현재 판매자의 제품 등록 요청
     * @header Seller's AccessToken
     * @body PostProductRequestDTO, MultipartFile[]
     * @response String
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
     * [GET]
     * 상품ID를 통해 상품의 상세 정보를 확인합니다.
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @response GetProductDetailResponseDTO
     */
    @Operation(summary = "상품 상세 정보 확인", description = "상품ID를 통해 상품의 상세 정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 조회에 성공했을 때"),
            @ApiResponse(responseCode = "404", description = "해당 productId를 가진 상품이 존재하지 않을 때", content = @Content(schema = @Schema(defaultValue = "해당 productId를 가진 상품이 존재하지 않습니다.")))
    })
    @GetMapping("/sellers/products/{productId}")
    public BaseResponse<GetProductDetailResponseDTO> getProductDetail(@PathVariable Long productId) {
        GetProductDetailResponseDTO productDetail = productApplication.getProductDetail(productId);
        return new BaseResponse<>(productDetail);
    }

    /**
     * [GET]
     * 현재 판매자의 상품 정보 조회
     * @header Seller's AccessToken
     * @response GetSellerProductsResponseDTO
     */
    @GetMapping("/sellers/products")
    public BaseResponse<GetSellerProductsResponseDTO> getSellerProducts(){
        return new BaseResponse<>(productApplication.getSellerProducts());
    }

    /**
     * [PATCH]
     * 현재 판매자의 제품 정보를 수정 요청
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @param productImages 수정할 이미지 파일 리스트
     * @param postProductRequestDto 수정할 제품 정보 DTO
     * @response String
     */
    @PatchMapping("/sellers/products/{productId}")
    public BaseResponse<String> patchProduct(
            @PathVariable Long productId,
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") PostProductRequestDTO postProductRequestDto
    ) {
        productApplication.patchProduct(postProductRequestDto, productImages, productId);
        return new BaseResponse<>("수정 성공");
    }

    /**
     * [DELETE]
     * 상품 삭제 요청
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @DeleteMapping("/sellers/products/{productId}")
    public BaseResponse<String> deleteProduct(@PathVariable Long productId) {
        productApplication.deleteProduct(productId);
        return new BaseResponse<>("상품 삭제가 완료되었습니다.");
    }

    /**
     * [POST]
     * 상품 좋아요
     * @header Customer's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PostMapping("/customers/products/{productId}/like")
    public BaseResponse<String> postLikeProduct(@PathVariable Long productId) {
        productApplication.postProductLike(productId);
        return new BaseResponse<>("상품의 좋아요가 완료되었습니다.");
    }

    /**
     * [DELETE]
     * 상품 좋아요 취소
     * @header Customer's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @DeleteMapping("/customers/products/{productId}/like")
    public BaseResponse<String> deleteLikeProduct(@PathVariable Long productId) {
        productApplication.deleteProductLike(productId);
        return new BaseResponse<>("상품의 좋아요가 취소되었습니다.");
    }

    /**
     * [PATCH]
     * 관리자가 등록된 상품들의 승인 상태 '거부'로 변경.
     * @header Admin's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PatchMapping("/admins/product-requests/{productId}/deny")
    public BaseResponse<String> denyProductRegister(@PathVariable Long productId) {
        productApplication.denyProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 거부됐습니다.");
    }

    /**
     * [PATCH]
     * 관리자가 등록된 상품들의 승인 상태 '승인'으로 변경.
     * @header Admin's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PatchMapping("/admins/product-requests/{productId}/approve")
    public BaseResponse<String> approveProductRegister(@PathVariable Long productId) {
        productApplication.approveProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 승인됐습니다.");
    }

    /**
     * [PATCH]
     * 관리자가 등록된 상품들의 승인 상태 '대기'로 변경.
     * @header Admin's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PatchMapping("/admins/product-requests/{productId}/pending")
    public BaseResponse<String> pendingProductRegister(@PathVariable Long productId) {
        productApplication.pendingProduct(productId);
        return new BaseResponse<>("해당 상품 등록이 대기 상태로 변경됐습니다.");
    }

    /**
     * [GET]
     * 관리자가 등록된 상품들의 요청들을 상태별(AuthorityStatus) 페이징 조회할 수 있음.
     * @header Admin's AccessToken
     * @param authorityStatus 요청 상태
     * @param pageNo 페이지 번호
     * @response List<GetProductRequestByStatusResponseDTO>
     */
    @GetMapping("/admins/product-requests")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getProductRequestsByStatus(
            @RequestParam(value = "authorityStatus", required = false) AuthorityStatus authorityStatus,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(authorityStatus, pageNo));
    }

    /**
     * [GET]
     * 승인된 상품 요청 조회
     * @header Admin's AccessToken
     * @param pageNo 페이지 번호
     * @response List<GetProductRequestByStatusResponseDTO>
     */
    @GetMapping("/admins/product-requests/approved")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getApprovedProductRequests(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(AuthorityStatus.APPROVED, pageNo));
    }

    /**
     * [GET]
     * 대기 중인 상품 요청 조회
     * @header Admin's AccessToken
     * @param pageNo 페이지 번호
     * @response List<GetProductRequestByStatusResponseDTO>
     */
    @GetMapping("/admins/product-requests/pending")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getPendingProductRequests(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(AuthorityStatus.PENDING, pageNo));
    }

    /**
     * [GET]
     * 거부된 상품 요청 조회
     * @header Admin's AccessToken
     * @param pageNo 페이지 번호
     * @response List<GetProductRequestByStatusResponseDTO>
     */
    @GetMapping("/admins/product-requests/denied")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getDeniedProductRequests(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(AuthorityStatus.DENIED, pageNo));
    }

    /**
     * [GET]
     * 판매자 판매 현황 조회
     * @header Admin's AccessToken
     * @param sellerId 판매자 ID
     * @response SellerSalesResponseDTO
     */
    @GetMapping("admins/sellers/{sellerId}/sales")
    public BaseResponse<SellerSalesResponseDTO> getSalesBySellerId(@PathVariable Long sellerId) {
        SellerSalesResponseDTO salesResponse = productApplication.getSalesBySellerId(sellerId);
        return new BaseResponse<>(salesResponse);
    }
}
