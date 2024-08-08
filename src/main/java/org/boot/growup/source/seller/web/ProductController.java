package org.boot.growup.source.seller.web;

import org.boot.growup.source.seller.dto.ProductRequestDTO;
import org.boot.growup.source.seller.dto.ProductResponseDTO;
import org.boot.growup.source.seller.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.Valid;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@RestController
@RequestMapping("/sellers")
public class ProductController {

    private final ProductService productService;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProductController(ProductService productService, ObjectMapper objectMapper) {
        this.productService = productService;
        this.objectMapper = objectMapper;
    }

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
