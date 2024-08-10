package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.seller.application.BrandApplication;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.ReadSellerBrandResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sellers/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandApplication brandApplication;

    /**
     * 현재 판매자의 브랜드 등록 요청
     * @param brandImageFiles
     * @param registerBrandRequestDTO
     * @return
     */
    @PostMapping
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
    @GetMapping
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
    @PatchMapping
    public BaseResponse<String> updateBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") RegisterBrandRequestDTO registerBrandRequestDTO
    ){
        brandApplication.updateBrand(registerBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("수정 성공");
    }





}
