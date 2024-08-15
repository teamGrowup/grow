package org.boot.growup.source.seller.application;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;
import org.boot.growup.source.seller.dto.response.ProductDetailResponseDTO;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO.ProductOptionDTO;
import org.boot.growup.source.seller.dto.SubCategoryDTO;
import org.boot.growup.source.seller.dto.MainCategoryDTO;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
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
    @Mock
    private BrandRepository brandRepository; // BrandRepository Mock 추가

    private PostProductRequestDTO postProductRequestDTO;
    private Product product;
    private Seller seller;
    private SubCategory subCategory;

    private MainCategory mainCategory;
    private ProductImage productImage;
    private SubCategoryDTO subCategoryDTO;
    private MainCategoryDTO mainCategoryDTO;

    Brand brand1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // MainCategoryDTO 초기화
        mainCategoryDTO = new MainCategoryDTO(1L, "상의");

        // SubCategoryDTO 초기화
        subCategoryDTO = new SubCategoryDTO(1L, "반팔", mainCategoryDTO);

        brand1 = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .build();

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
        postProductRequestDTO = PostProductRequestDTO.builder()
                .name("테스트 상품")
                .description("테스트 상품 설명입니다.")
                .subCategoryId(subCategory.getId()) // 서브 카테고리 ID 추가
                .brandId(brand1.getId())
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
                .brand(brand1)
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
    public void postProductWithImages_Default_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "product_image1.jpg", "image/jpeg", "image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product_image2.jpg", "image/jpeg", "image content 2".getBytes());
        given(productService.registerProduct(postProductRequestDTO, seller)).willReturn(product);
        given(sellerRepository.findById(seller.getId())).willReturn(Optional.of(seller));

        given(brandRepository.findById(brand1.getId())).willReturn(Optional.of(brand1));
        List<MultipartFile> mockFiles = List.of(file1, file2);

        // when
        productApplication.postProductWithImages(postProductRequestDTO, mockFiles);

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
    void testPatchProduct() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());
        List<MultipartFile> mockFiles = List.of(file1, file2);

        given(productService.patchProduct(postProductRequestDTO, seller, product.getId())).willReturn(product);
        given(sellerRepository.findById(seller.getId())).willReturn(Optional.of(seller));
        given(brandRepository.findById(brand1.getId())).willReturn(Optional.of(brand1));

        // when
        productApplication.patchProduct(postProductRequestDTO, mockFiles, product.getId());

        // then
        verify(sellerRepository).findById(seller.getId());
        verify(productService).patchProduct(postProductRequestDTO, seller, product.getId());
        verify(productImageService).patchProductImages(mockFiles, product, Section.PRODUCT_IMAGE);
    }

}
