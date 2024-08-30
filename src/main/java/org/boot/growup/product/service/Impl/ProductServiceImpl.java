package org.boot.growup.product.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.persist.repository.SellerRepository;
import org.boot.growup.common.constant.OrderStatus;
import org.boot.growup.common.constant.Section;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.order.dto.OrderItemDTO;
import org.boot.growup.common.utils.ImageStore;
import org.boot.growup.common.utils.S3Service;
import org.boot.growup.order.persist.entity.OrderItem;
import org.boot.growup.order.persist.repository.OrderItemRepository;
import org.boot.growup.product.dto.response.ProductSalesResponseDTO;
import org.boot.growup.product.dto.response.SellerSalesResponseDTO;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.product.persist.repository.*;
import org.boot.growup.product.service.ProductService;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toUnmodifiableMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final BrandRepository brandRepository;
    private final ProductLikeRepository productLikeRepository;
    private final ProductOptionRepository productOptionRepository;
    private final ProductImageRepository productImageRepository;
    private final SellerRepository sellerRepository;
    private final OrderItemRepository orderItemRepository;
    private final ImageStore imageStore;
    private final S3Service s3Service;

    @Override
    public Product postProduct(PostProductRequestDTO postProductRequestDto, Seller seller) {
        // 서브 카테고리 가져오기
        SubCategory subCategory = subCategoryRepository.findById(postProductRequestDto.getSubCategoryId())
                .orElseThrow(() -> new BaseException(ErrorCode.SUBCATEGORY_NOT_FOUND));
        // 브랜드 가져오기
        Brand brand = brandRepository.findById(postProductRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));
        Product product = Product.of(postProductRequestDto, brand, subCategory);

        // 상품 옵션 설정
        List<ProductOption> productOptions = convertToProductOptions(postProductRequestDto.getProductOptions(), product);
        product.initProductOptions(productOptions); // 상품 옵션 초기화 메서드 사용

        product.patchSubCategory(subCategory); // 서브 카테고리 설정
        product.patchBrand(brand);
        product.pending();
        product.initAverageRating();
        product.initLikeCount();
        product.initDeliveryFee();
        product.designateSeller(seller); // 판매자 설정.

        productRepository.save(product);
        return product;
    }
    @Override
    public Optional<Product> getProductById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void deleteProductById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySeller_Id(sellerId);

        // 리스트가 비어 있는 경우 예외 발생
        if (products.isEmpty()) {
            throw new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND);
        }
        return products;
    }

    // ProductRequestDTO의 ProductOptionDTO를 ProductOption으로 변환하는 메서드
    private List<ProductOption> convertToProductOptions(List<PostProductRequestDTO.ProductOptionDTO> productOptionDTOs, Product product) {
        return productOptionDTOs.stream()
                .map(dto -> ProductOption.builder()
                        .optionName(dto.getOptionName())
                        .optionStock(dto.getOptionStock())
                        .optionPrice(dto.getOptionPrice())
                        .product(product) // Product 설정 추가
                        .build())
                .toList();
    }

    @Override
    public Product patchProduct(PostProductRequestDTO postProductRequestDto, Seller seller, Long productId) {
        // 판매자ID와 상품 ID로 상품 조회
        Product product = productRepository.findByIdAndSeller_Id(productId, seller.getId())
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_BY_SELLER_NOT_FOUND));

        // Brand 조회
        Brand brand = brandRepository.findById(postProductRequestDto.getBrandId())
                .orElseThrow(() -> new BaseException(ErrorCode.BRAND_BY_ID_NOT_FOUND));

        // 상태 변경 및 정보 업데이트
        product.pending(); // 대기 상태로 변경.
        product.patchProductInfo(postProductRequestDto.getName(), postProductRequestDto.getDescription());
        product.patchBrand(brand); // Brand 정보 업데이트

        // 상품 저장
        productRepository.save(product);
        return product;
    }

    @Override
    public List<ProductOption> getProductOptions(Long productId) {
        return productOptionRepository.findByProductId(productId);
    }

    @Override
    public void changeProductAuthority(Long productId, AuthorityStatus status) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        switch (status) {
            case DENIED -> product.deny();
            case PENDING -> product.pending();
            case APPROVED -> product.approve();
        }
        productRepository.save(product);
    }

    @Override
    public List<Product> getProductRequestsByStatus(AuthorityStatus authorityStatus, int pageNo) {
        Pageable pageable = PageRequest.of(pageNo, 10);

        if (authorityStatus == null) {
            return productRepository.findAll(pageable).stream().toList();
        }

        return productRepository.findByAuthorityStatus(authorityStatus, pageable);
    }

    public void postProductLike(Long productId, Customer customer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        ProductLike productLike = ProductLike.builder()
                .customer(customer)
                .product(product)
                .build();

        product.likeCountPlus();

        productLikeRepository.save(productLike);
    }

    public void deleteProductLike(Long productId, Customer customer) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND));

        // 좋아요 정보 찾기
        ProductLike productLike = productLikeRepository.findByCustomerAndProduct(customer, product)
                .orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_LIKE_NOT_FOUND));

        // 좋아요 수 감소
        product.likeCountMinus();

        // 좋아요 정보 삭제
        productLikeRepository.delete(productLike);
    }

    @Override
    public List<ProductImage> getProductImages(Long id) {
        return productImageRepository.findProductImageByProduct_Id(id);
    }

    public ProductImage storeImage(MultipartFile multipartFile, Section section) {
        if (multipartFile.isEmpty()) {
            throw new IllegalStateException("이미지가 없습니다.");
        }

        String originalFilename = multipartFile.getOriginalFilename(); // 원래 이름
        String storeFilename = imageStore.createStoreFileName(originalFilename); // 저장된 이름
        String path;

        try {
            path = s3Service.uploadFileAndGetUrl(multipartFile, storeFilename); // S3에 업로드
        } catch (IOException e) {
            throw new RuntimeException("S3 업로드 중 오류 발생", e);
        }

        // 로그 출력
        System.out.println("Section value: " + section);

        return ProductImage.builder()
                .originalImageName(originalFilename)
                .path(path) // S3 경로 저장
                .section(section) // 섹션 설정
                .build();
    }

    @Override
    public Map<ProductOption, Integer> getProductOptionCountMap(List<OrderItemDTO> orderItemDTOs) {
        // 중복된 ProductOption 검사 및 해당 ProductOption이 없는지 검사
        return orderItemDTOs.stream()
                .collect(
                    toUnmodifiableMap(
                            m -> productOptionRepository.findById(m.getProductOptionId()).orElseThrow(() -> new BaseException(ErrorCode.PRODUCT_NOT_FOUND)),
                            OrderItemDTO::getCount
                )
        );
    }

    public void postProductImages(List<MultipartFile> productImages, Product product, Section section) {
        for (MultipartFile multipartFile : productImages) {
                ProductImage uploadImage = storeImage(multipartFile, section);
                uploadImage.designateProduct(product);
                product.getProductImages().add(uploadImage);
                productImageRepository.save(uploadImage);
        }
    }

    public void patchProductImages(List<MultipartFile> productImages, Product product, Section section) {
        // 1. 현재 S3에 등록된 상품 이미지를 지움.
        productImageRepository.findProductImageByProduct_Id(product.getId()).forEach(m -> s3Service.deleteFile(m.getPath()));

        // 2. DB에 있는 상품 이미지 삭제.
        productImageRepository.deleteProductImageByProduct_Id(product.getId());

        // 3. 해당 상품에 이미지를 새로 등록함.
        for (MultipartFile multipartFile : productImages) {
            if (!multipartFile.isEmpty()) {
                ProductImage uploadImage = storeImage(multipartFile, section); // 이미지 저장
                uploadImage.designateProduct(product); // 상품 설정
                product.getProductImages().add(uploadImage); // Product에 추가
                productImageRepository.save(uploadImage); // 저장 시도
            }
        }
    }

    @Override
    public SellerSalesResponseDTO getSalesBySellerId(Long sellerId) {
        // 판매자 정보 조회
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new BaseException(ErrorCode.SELLER_NOT_FOUND));

        // 구매 확정 상태의 주문 항목 조회
        List<OrderItem> confirmedOrderItems = orderItemRepository.findBySellerIdAndOrderStatus(sellerId, OrderStatus.PURCHASE_CONFIRM);

        // 판매 현황 초기화
        int totalQuantity = 0;
        int totalRevenue = 0;
        List<ProductSalesResponseDTO> productSales = new ArrayList<>();

        for (OrderItem orderItem : confirmedOrderItems) {
            // 각 상품의 판매 수량 및 총 수익 계산
            int quantitySold = orderItem.getCount();
            int revenue = quantitySold * orderItem.getProductOptionPrice(); // 상품 옵션 가격을 사용하여 수익 계산

            totalQuantity += quantitySold;
            totalRevenue += revenue;

            productSales.add(new ProductSalesResponseDTO(orderItem.getProduct().getId(), orderItem.getProductName(), quantitySold, revenue));
        }

        return new SellerSalesResponseDTO(sellerId, seller.getEpName(), productSales, totalQuantity, totalRevenue);
    }
}