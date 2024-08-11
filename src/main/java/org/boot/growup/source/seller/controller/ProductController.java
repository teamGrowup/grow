package org.boot.growup.source.seller.controller;

import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.seller.application.ProductApplication;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/sellers/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductApplication productApplication;

    /**
     * 현재 판매자의 제품 등록 요청
     * @param productImages
     * @param productRequestDto
     * @return BaseResponse<String>
     */
    @PostMapping(consumes = "multipart/form-data")
    public BaseResponse<String> registerProduct(
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") ProductRequestDTO productRequestDto
    ) {
        productApplication.registerProductWithImages(productRequestDto, productImages);
        return new BaseResponse<>("등록 성공");
    }

    /**
     * 상품ID를 통해 상품의 상세 정보를 확인합니다.
     * @param productId 상품 ID
     * @return BaseResponse<ProductDetailResponseDTO>
     */
    @GetMapping("/{productId}")
    public BaseResponse<ProductDetailResponseDTO> getProductDetail(@PathVariable Long productId) {
        ProductDetailResponseDTO productDetail = productApplication.getProductDetail(productId);
        return new BaseResponse<>(productDetail);
    }

    /**
     * 현재 판매자의 제품 정보를 수정 요청
     * @param productId 상품 ID
     * @param productImages 수정할 이미지 파일 리스트
     * @param productRequestDto 수정할 제품 정보 DTO
     * @return BaseResponse<String>
     */
    @PatchMapping("/{productId}")
    public BaseResponse<String> updateProduct(
            @PathVariable Long productId,
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") ProductRequestDTO productRequestDto
    ) {
        productApplication.updateProduct(productId, productRequestDto, productImages);
        return new BaseResponse<>("수정 성공");
    }

}
