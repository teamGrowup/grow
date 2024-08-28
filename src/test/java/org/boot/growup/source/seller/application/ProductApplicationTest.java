package org.boot.growup.source.seller.application;

import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.auth.service.SellerService;
import org.boot.growup.auth.service.CustomerService;
import org.boot.growup.common.constant.Section;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.application.ProductApplication;
import org.boot.growup.product.dto.MainCategoryDTO;
import org.boot.growup.product.dto.SubCategoryDTO;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.dto.response.GetProductDetailResponseDTO;
import org.boot.growup.product.dto.response.GetSellerProductsResponseDTO;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.product.persist.repository.ProductRepository;
import org.boot.growup.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductApplicationTest {
    @InjectMocks
    private ProductApplication productApplication;

    @Mock
    private ProductService productService;

    @Mock
    private SellerService sellerService;

    @Mock
    private CustomerService customerService;

    @Mock
    private ProductRepository productRepository;

    private PostProductRequestDTO postProductRequestDTO;
    private MainCategoryDTO mainCategoryDTO;
    private SubCategoryDTO subCategoryDTO;
    private Product product;
    private Seller seller;
    private SubCategory subCategory;
    private MainCategory mainCategory;
    private ProductImage productImage;

    Brand brand1;

    @BeforeEach
    void setUp() {
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
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("테스트 옵션")
                                .optionStock(10)
                                .optionPrice(10000)
                                .build(),
                        PostProductRequestDTO.ProductOptionDTO.builder()
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
    public void postProductWithImages_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "product_image1.jpg", "image/jpeg", "image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product_image2.jpg", "image/jpeg", "image content 2".getBytes());
        List<MultipartFile> mockFiles = List.of(file1, file2);

        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(productService.postProduct(postProductRequestDTO, seller)).willReturn(product);

        // when
        productApplication.postProductWithImages(postProductRequestDTO, mockFiles);

        // then
        verify(productService).postProductImages(mockFiles, product, Section.PRODUCT_IMAGE);
    }

    @Test
    void getSellerProducts_Success() {
        // Given
        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(productService.getProductsBySellerId(seller.getId())).willReturn(List.of(product));

        // When
        GetSellerProductsResponseDTO response = productApplication.getSellerProducts();

        // Then
        assertNotNull(response);
        assertEquals(1, response.getProducts().size());
        assertEquals("진짜 반팔", response.getProducts().get(0).getName());
    }

    @Test
    void getProductDetail_Success() {
        // Given
        Long productId = product.getId(); // Mock에서 사용할 상품 ID
        // Mock 설정
        given(productService.getProductById(productId)).willReturn(Optional.of(product)); // Ensure the service returns the product

        // When
        GetProductDetailResponseDTO response = productApplication.getProductDetail(productId);

        // Then
        assertNotNull(response);
        assertEquals("진짜 반팔", response.getName());
        assertEquals("순도 100%의 반팔입니다. 긴팔도 아니고 나시도 아니고 진짜 반팔이에요.", response.getDescription());
    }

    @Test
    void getProductDetail_ProductNotFound() {
        // Given
        given(productRepository.findById(product.getId())).willReturn(Optional.empty());

        // When and Then
        assertThrows(BaseException.class, () -> {
            productApplication.getProductDetail(product.getId());
        });
    }

    @Test
    void patchProduct_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        List<MultipartFile> mockFiles = List.of(file1);

        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(productService.patchProduct(postProductRequestDTO, seller, product.getId())).willReturn(product);

        // when
        productApplication.patchProduct(postProductRequestDTO, mockFiles, product.getId());

        // then
        verify(productService).patchProductImages(mockFiles, product, Section.PRODUCT_IMAGE);
    }

    @Test
    void deleteProduct_Success() {
        // Given
        Long productId = product.getId(); // 삭제할 상품 ID

        // When
        productApplication.deleteProduct(productId); // deleteProduct 메서드 호출

        // Then
        verify(productService).deleteProductById(productId); // productService의 deleteProductById가 호출되었는지 검증
    }


    @Test
    void approveProduct_Success() {
        // when
        productApplication.approveProduct(product.getId());

        // then
        verify(productService).changeProductAuthority(product.getId(), AuthorityStatus.APPROVED);
    }

    @Test
    void denyProduct_Success() {
        // when
        productApplication.denyProduct(product.getId());

        // then
        verify(productService).changeProductAuthority(product.getId(), AuthorityStatus.DENIED);
    }

    @Test
    void pendingProduct_Success() {
        // when
        productApplication.pendingProduct(product.getId());

        // then
        verify(productService).changeProductAuthority(product.getId(), AuthorityStatus.PENDING);
    }

    @Test
    void postProductLike_Success() {
        // given
        Customer customer = new Customer(); // Mock or initialize Customer as needed
        given(customerService.getCurrentCustomer()).willReturn(customer);

        // when
        productApplication.postProductLike(product.getId());

        // then
        verify(productService).postProductLike(product.getId(), customer);
    }

    @Test
    void deleteProductLike_Success() {
        // given
        Customer customer = new Customer(); // Mock or initialize Customer as needed
        given(customerService.getCurrentCustomer()).willReturn(customer);

        // when
        productApplication.deleteProductLike(product.getId());

        // then
        verify(productService).deleteProductLike(product.getId(), customer);
    }
}