package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.seller.application.BrandApplication;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sellers/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandApplication brandApplication;

    @PostMapping
    public BaseResponse<String> registerBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") RegisterBrandRequestDTO registerBrandRequestDTO
    ) {
        brandApplication.registerBrandWithBrandImages(registerBrandRequestDTO, brandImageFiles);
        return new BaseResponse<>("등록 성공");
    }
}
