package org.boot.growup.product.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.dto.request.PostBrandRequestDTO;
import org.boot.growup.product.dto.response.GetSellerBrandResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins/brand-requests")
@RequiredArgsConstructor
public class SellerBrandController {
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
     * @return GetSellerBrandResponseDTO
     */
    @GetMapping("/sellers/brands")
    public BaseResponse<GetSellerBrandResponseDTO> getSellerBrand(){
        return new BaseResponse<>(brandApplication.getSellerBrand());
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

}
