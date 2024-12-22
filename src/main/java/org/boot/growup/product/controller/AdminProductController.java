package org.boot.growup.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.ProductApplication;
import org.boot.growup.product.dto.response.GetProductRequestByStatusResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins/product-requests")
@RequiredArgsConstructor
public class AdminProductController {
    private final ProductApplication productApplication;

    /**
     * [PATCH]
     * 관리자가 등록된 상품들의 승인 상태 '거부'로 변경.
     * @header Admin's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @PatchMapping("/{productId}/deny")
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
    @PatchMapping("/{productId}/approve")
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
    @PatchMapping("/{productId}/pending")
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
    @GetMapping
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
    @GetMapping("/approved")
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
    @GetMapping("/pending")
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
    @GetMapping("/denied")
    public BaseResponse<List<GetProductRequestByStatusResponseDTO>> getDeniedProductRequests(
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ) {
        return new BaseResponse<>(productApplication.getProductRequestsByStatus(AuthorityStatus.DENIED, pageNo));
    }
}
