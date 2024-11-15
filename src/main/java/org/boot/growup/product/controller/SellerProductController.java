package org.boot.growup.product.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.model.BaseResponse;
import org.boot.growup.product.application.ProductApplication;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.dto.response.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SellerProductController {
    private final ProductApplication productApplication;

    /**
     * [POST]
     * 현재 판매자의 제품 등록 요청
     * @header Seller's AccessToken
     * @body PostProductRequestDTO, MultipartFile[]
     * @response String
     */
    @PostMapping("/sellers/products")
    public BaseResponse<String> postProduct(
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") PostProductRequestDTO postProductRequestDto
    ) {
        productApplication.postProductWithImages(postProductRequestDto, productImages);
        return new BaseResponse<>("등록 성공");
    }

    /**
     * [GET]
     * 상품ID를 통해 상품의 상세 정보를 확인합니다.
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @response GetProductDetailResponseDTO
     */
    @Operation(summary = "상품 상세 정보 확인", description = "상품ID를 통해 상품의 상세 정보를 확인합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "상품 조회에 성공했을 때"),
            @ApiResponse(responseCode = "404", description = "해당 productId를 가진 상품이 존재하지 않을 때", content = @Content(schema = @Schema(defaultValue = "해당 productId를 가진 상품이 존재하지 않습니다.")))
    })
    @GetMapping("/sellers/products/{productId}")
    public BaseResponse<GetProductDetailResponseDTO> getProductDetail(@PathVariable Long productId) {
        GetProductDetailResponseDTO productDetail = productApplication.getProductDetail(productId);
        return new BaseResponse<>(productDetail);
    }

    /**
     * [GET]
     * 현재 판매자의 상품 정보 조회
     * @header Seller's AccessToken
     * @response GetSellerProductsResponseDTO
     */
    @GetMapping("/sellers/products")
    public BaseResponse<GetSellerProductsResponseDTO> getSellerProducts(){
        return new BaseResponse<>(productApplication.getSellerProducts());
    }

    /**
     * [PATCH]
     * 현재 판매자의 제품 정보를 수정 요청
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @param productImages 수정할 이미지 파일 리스트
     * @param postProductRequestDto 수정할 제품 정보 DTO
     * @response String
     */
    @PatchMapping("/sellers/products/{productId}")
    public BaseResponse<String> patchProduct(
            @PathVariable Long productId,
            @RequestPart(value = "images", required = false) List<MultipartFile> productImages,
            @RequestPart(value = "form") PostProductRequestDTO postProductRequestDto
    ) {
        productApplication.patchProduct(postProductRequestDto, productImages, productId);
        return new BaseResponse<>("수정 성공");
    }

    /**
     * [DELETE]
     * 상품 삭제 요청
     * @header Seller's AccessToken
     * @param productId 상품 ID
     * @response String
     */
    @DeleteMapping("/sellers/products/{productId}")
    public BaseResponse<String> deleteProduct(@PathVariable Long productId) {
        productApplication.deleteProduct(productId);
        return new BaseResponse<>("상품 삭제가 완료되었습니다.");
    }
}
