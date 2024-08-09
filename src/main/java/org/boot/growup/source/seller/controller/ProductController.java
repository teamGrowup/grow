package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.application.ProductApplication;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sellers/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplication productApplication;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<?> registerProduct(
            @RequestPart(value = "images") List<MultipartFile> productImages,
            @RequestPart(value = "form") ProductRequestDTO productRequestDto
    ) {
        productApplication.registerProductWithImages(productRequestDto, productImages);
        return ResponseEntity.ok("등록 성공");
    }
}
