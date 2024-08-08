package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.application.BrandApplication;
import org.boot.growup.source.seller.dto.BrandPostDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sellers/brand")
@RequiredArgsConstructor
public class BrandController {

    private final BrandApplication brandApplication;

    @PostMapping
    public ResponseEntity<?> registerBrand(
            @RequestPart(value = "brandImages") List<MultipartFile> brandImageFiles,
            @RequestPart(value = "form") BrandPostDTO brandPostDTO
    ) {
        brandApplication.registerBrandWithBrandImages(brandPostDTO, brandImageFiles);
        return ResponseEntity.ok("등록 성공");
    }
}
