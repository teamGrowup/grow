package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.application.BrandApplication;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.ReadBrandRequestByStatusResponseDTO;
import org.boot.growup.source.seller.dto.response.ReadSellerBrandResponseDTO;
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
     * @param registerBrandRequestDTO
     * @return
     */
    @PostMapping("/sellers/brand")
    public BaseResponse<String> registerBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") RegisterBrandRequestDTO registerBrandRequestDTO
    ) {
        brandApplication.registerBrandWithBrandImages(registerBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("등록 성공");
    }

    /**
     * 현재 판매자의 브랜드 정보 조회
     * @return ReadSellerBrandResponseDTO
     */
    @GetMapping("/sellers/brand")
    public BaseResponse<ReadSellerBrandResponseDTO> readSellerBrand(){
        var res = brandApplication.readSellerBrand();
        return new BaseResponse<>(res);
    }

    /**
     * 현재 판매자의 브랜드 정보 수정 및 허가요청
     * @param brandImageFiles
     * @param registerBrandRequestDTO
     * @return
     */
    @PatchMapping("/sellers/brand")
    public BaseResponse<String> updateBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") RegisterBrandRequestDTO registerBrandRequestDTO
    ){
        brandApplication.updateBrand(registerBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("수정 성공");
    }

    /**
     * 관리자가 등록된 brand들의 승인 상태 '거부'로 변경.
     * @param brandId
     * @return
     */
    @PatchMapping("/admin/brand-requests/{brandId}/deny")
    public BaseResponse<String> denyBrandRegister(@PathVariable Long brandId){
        brandApplication.denyBrandRegister(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 거부됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 승인 상태 '승인'으로 변경.
     * @param brandId
     * @return
     */
    @PatchMapping("/admin/brand-requests/{brandId}/approve")
    public BaseResponse<String> approveBrandRegister(@PathVariable Long brandId){
        brandApplication.approveBrandRegister(brandId);
        return new BaseResponse<>("해당 브랜드 등록이 승인됐습니다.");
    }

    /**
     * 관리자가 등록된 brand들의 요청들을 상태별(AuthorityStatus) 페이징 조회할 수 있음.
     * @param authorityStatus
     * @param pageNo
     * @return
     */
    @GetMapping("/admin/brand-requests")
    public BaseResponse<List<ReadBrandRequestByStatusResponseDTO>> readBrandRequestsByStatus(
            @RequestParam(value = "authorityStatus", required = false) AuthorityStatus authorityStatus,
            @RequestParam(value = "pageNo", defaultValue = "0") int pageNo
    ){
        return new BaseResponse<>(brandApplication.readBrandRequestByStatus(authorityStatus, pageNo));
    }


}
