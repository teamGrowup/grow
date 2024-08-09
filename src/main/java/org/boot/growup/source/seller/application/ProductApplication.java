package org.boot.growup.source.seller.application;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductResponseDTO;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.service.ProductImageServiceImpl;
import org.boot.growup.source.seller.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductApplication {

    private final ProductService productService;
    private final ProductImageServiceImpl productImageService;
    private final ProductRepository productRepository;

    @Transactional
    public ProductResponseDTO registerProductWithImages(ProductRequestDTO productRequestDto, List<MultipartFile> productImages) {
        // TODO: 현재 유저가 seller인지 확인 및 seller 가져오기.
        // 예: Seller seller = sellerService.getCurrentSeller();

        // TODO: 해당 seller가 상품을 등록할 수 있는지 검사.
        // 예: if (!seller.canRegisterProduct()) { throw new IllegalArgumentException("등록할 수 없는 상태입니다."); }

        // 상품 등록 및 ProductResponseDTO 반환
        ProductResponseDTO response = productService.registerProduct(productRequestDto, productImages);

        // 등록된 상품 ID로 Product 객체를 가져오기
        Long productId = response.getProductId(); // productId를 가져옵니다.
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("상품 등록 후 Product를 찾을 수 없습니다."));

        // 이미지 저장
        productImageService.saveProductImages(productImages, product);

        return response; // 성공적으로 등록된 상품에 대한 응답 반환
    }


}
