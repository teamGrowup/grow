package org.boot.growup.source.seller.web;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.service.ProductService;
import org.boot.growup.source.seller.service.ProductServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/sellers")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
//    private final ProductServiceImpl productServiceImpl;
    private final ObjectMapper objectMapper;

    @PostMapping("/products")
    public ResponseEntity<ProductResponseDTO> registerProduct(
            @RequestPart("product") String productJson, // JSON 문자열로 받음
            @RequestPart(value = "images", required = false) List<MultipartFile> images) {

        // JSON 문자열을 ProductRequestDTO로 변환
        ProductRequestDTO productRequestDto;
        try {
            productRequestDto = objectMapper.readValue(productJson, ProductRequestDTO.class);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ProductResponseDTO("등록 실패", null));
        }

        // 상품 등록 서비스 호출
        ProductResponseDTO response = productService.registerProduct(productRequestDto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
