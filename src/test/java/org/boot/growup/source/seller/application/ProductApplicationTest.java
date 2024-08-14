package org.boot.growup.source.seller.application;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO.ProductOptionDTO;
import org.boot.growup.source.seller.dto.SubCategoryDTO;
import org.boot.growup.source.seller.dto.MainCategoryDTO;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.service.ProductImageServiceImpl;
import org.boot.growup.source.seller.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class ProductApplicationTest {

    @InjectMocks
    private ProductApplication productApplication;

    @Mock
    private ProductService productService;

    @Mock
    private ProductImageServiceImpl productImageService;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SellerRepository sellerRepository;

    private ProductRequestDTO productRequestDTO;
    private Product product;
    private Seller seller;
    private SubCategory subCategory;

    private MainCategory mainCategory;
    private ProductImage productImage;
    private SubCategoryDTO subCategoryDTO;
    private MainCategoryDTO mainCategoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // MainCategoryDTO 초기화
        mainCategoryDTO = new MainCategoryDTO(1L, "상의");

        // SubCategoryDTO 초기화
        subCategoryDTO = new SubCategoryDTO(1L, "반팔", mainCategoryDTO);

        mainCategory = MainCategory.builder()
                .name("상의")
                .build();

        subCategory = SubCategory.builder()
                .name("반팔")
                .mainCategory(mainCategory)
                .build();

        // Seller 초기화
        seller = Seller.builder()
                .id(1L) // ID 추가
                .cpEmail("test@example.com")
                .cpPassword("password123")
                .phoneNumber("010-1234-5678")
                .epName("테스트 대표자")
                .cpName("테스트 상호명")
                .cpCode("123-45-67890")
                .cpAddress("테스트 주소")
                .netProceeds(1000)
                .build();

        // ProductRequestDTO 초기화
        productRequestDTO = ProductRequestDTO.builder()
                .name("테스트 상품")
                .description("테스트 상품 설명입니다.")
                .subCategoryId(subCategory.getId()) // 서브 카테고리 ID 추가
                .sellerId(seller.getId()) // 판매자 ID 추가
                .productOptions(Arrays.asList(
                        ProductOptionDTO.builder()
                                .optionName("테스트 옵션")
                                .optionStock(10)
                                .optionPrice(10000)
                                .build(),
                        ProductOptionDTO.builder()
                                .optionName("테스트 옵션2")
                                .optionStock(5)
                                .optionPrice(15000)
                                .build()
                ))
                .build();

        // Product 초기화
        product = Product.builder()
                .name("진짜 반팔")
                .description("순도 100%의 반팔입니다. 긴팔도 아니고 나시도 아니고 진짜 반팔이에요.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller) // 판매자 설정
                .build();

        // ProductImage 초기화
        productImage = ProductImage.builder()
                .path("aws/s3/path/test_image.jpg")
                .originalImageName("test_image.jpg")
                .build();
    }

    @Test
    public void registerProductWithImages_Default_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "product_image1.jpg", "image/jpeg", "image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product_image2.jpg", "image/jpeg", "image content 2".getBytes());
        given(productService.registerProduct(productRequestDTO, seller)).willReturn(product);
        given(sellerRepository.findById(seller.getId())).willReturn(Optional.of(seller));

        List<MultipartFile> mockFiles = List.of(file1, file2);

        // when
        productApplication.registerProductWithImages(productRequestDTO, mockFiles);

        // then
        verify(sellerRepository).findById(seller.getId());
        verify(productService).registerProduct(any(), eq(seller));
        verify(productImageService).saveProductImages(mockFiles, product, Section.PRODUCT_IMAGE);
    }


    @Test
    void getProductDetail_Default_Success() {
        // Given
        when(productRepository.findById(product.getId())).thenReturn(Optional.of(product));

        // When
        ProductDetailResponseDTO response = productApplication.getProductDetail(product.getId());

        // Then
        assertNotNull(response);
        assertEquals("진짜 반팔", response.getName());
    }

    @Test
    void testUpdateProduct() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        // Seller를 Mock으로 설정
        given(sellerRepository.findById(seller.getId())).willReturn(Optional.of(seller));

        // Product Repository에서 ID로 상품을 찾을 수 있도록 Mock 설정
        given(productRepository.findById(product.getId())).willReturn(Optional.of(product));

        // Product 객체가 업데이트된 후 반환될 것임을 Mock으로 설정
        given(productService.updateProduct(productRequestDTO, seller)).willReturn(product);

        List<MultipartFile> mockFiles = List.of(file1, file2);

        // when
        productApplication.updateProduct(productRequestDTO, mockFiles, seller.getId(), product.getId());

        // then
        verify(sellerRepository).findById(seller.getId());
        verify(productService).updateProduct(productRequestDTO, seller);
        verify(productImageService).updateProductImages(mockFiles, product, Section.PRODUCT_IMAGE);
    }



}
