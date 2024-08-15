package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.application.BrandApplication;
import org.boot.growup.source.seller.dto.request.PostBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.GetBrandDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetBrandRequestByStatusResponseDTO;
import org.boot.growup.source.seller.dto.response.getSellerBrandResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BrandController {

    private final BrandApplication brandApplication;

    /**
     * 현재 판매자의 브랜드 등록 요청
     * @param brandImageFiles
     * @param postBrandRequestDTO
     * @return String
     */
    @PostMapping("/sellers/brands")
    public BaseResponse<String> postBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") PostBrandRequestDTO postBrandRequestDTO
    ) {
        brandApplication.postBrandWithBrandImages(postBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("등록 성공");
    }

    /**
     * 현재 판매자의 브랜드 정보 조회
     * @return ReadSellerBrandResponseDTO
     */
    @GetMapping("/sellers/brands")
    public BaseResponse<getSellerBrandResponseDTO> getSellerBrand(){
        var res = brandApplication.getSellerBrand();
        return new BaseResponse<>(res);
    }

    /**
     * 현재 판매자의 브랜드 정보 수정 및 허가요청
     * @param brandImageFiles
     * @param postBrandRequestDTO
     * @return String
     */
    @PatchMapping("/sellers/brands")
    public BaseResponse<String> patchBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") PostBrandRequestDTO postBrandRequestDTO
    ){
        brandApplication.patchBrand(postBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("수정 성공");
    }

    /**
     * 관리자가 등록된 brand들의 승인 상태 '거부'로 변경.
     * @param brandId
     * @return String
     */
    @PatchMapping("/admins/brand-requests/{brandId}/deny")
    public BaseResponse<String> denyBrandPost(@PathVariable Long brandId){
        brandApplication.denyBrandPost(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 거부됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 승인 상태 '승인'으로 변경.
     * @param brandId
     * @return String
     */
    @PatchMapping("/admins/brand-requests/{brandId}/approve")
    public BaseResponse<String> approveBrandPost(@PathVariable Long brandId){
        brandApplication.approveBrandPost(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 승인됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 요청들을 상태별(AuthorityStatus) 페이징 조회할 수 있음.
     * @param authorityStatus
     * @param pageNo
     * @return List<ReadBrandRequestByStatusResponseDTO>
     */
    @GetMapping("/admins/brand-requests")
    public BaseResponse<List<GetBrandRequestByStatusResponseDTO>> getBrandRequestsByStatus(
            @RequestParam(value = "authorityStatus", required = false) AuthorityStatus authorityStatus,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ){
        return new BaseResponse<>(brandApplication.getBrandRequestByStatus(authorityStatus, pageNo));
    }

    /**
     * 브랜드 상세 조회 (설명, 이미지)
     * @param brandId
     * @return GetBrandDetailResponseDTO
     */
    @GetMapping("/customers/brands/brand/{brandId}")
    public BaseResponse<GetBrandDetailResponseDTO> getBrandDetail(@PathVariable Long brandId){
        return new BaseResponse<>(brandApplication.getBrandDetail(brandId));
    }

    // 브랜드 랭킹 순위별 조회 *좋아요 순

    // 브랜드 상품 조회
//    @GetMapping("/customers/brands/brand-products/{brandId}")
//    public BaseResponse<?> getBrandProducts(@PathVariable Long brandId){
//        return new BaseResponse<>(brandApplication.getBrandProducts());
//    }

    // 브랜드의 조건별 주문 내역 조회

    // 브랜드의 정산금(구매확정으로 인한 발생한 이익) 조회

    // 브랜드의 조건별(날짜, 상태) 주문 건수 조회


}
