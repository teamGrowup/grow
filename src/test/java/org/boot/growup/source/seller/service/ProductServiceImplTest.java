package org.boot.growup.source.seller.service;

import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.persist.repository.SellerRepository;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.Section;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.utils.ImageStore;
import org.boot.growup.common.utils.S3Service;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.product.persist.repository.*;
import org.boot.growup.product.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductServiceImplTest {

    @Autowired
    private ProductServiceImpl productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @MockBean
    private ProductImageRepository productImageRepository;

    @MockBean
    private ImageStore imageStore;

    @MockBean
    private S3Service s3Service;

    private Seller seller;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private Brand brand;

    @BeforeEach
    public void setup() {
        // Seller 초기화
        seller = Seller.builder()
                .cpEmail("test@seller.com")
                .cpPassword("password")
                .phoneNumber("010-1234-5678")
                .epName("테스트 업체")
                .cpName("테스트 연락처")
                .cpCode("123-45-67890")
                .cpAddress("테스트 주소")
                .netProceeds(1000)
                .role(Role.SELLER)
                .build();
        sellerRepository.save(seller); // Seller 저장

        // MainCategory 초기화
        mainCategory = MainCategory.builder()
                .name("상의")
                .build();
        mainCategoryRepository.save(mainCategory); // MainCategory 저장

        // SubCategory 초기화
        subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory)
                .build();
        subCategoryRepository.save(subCategory); // SubCategory 저장

        // Brand 초기화
        brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .build();
        brandRepository.save(brand); // Brand 저장
    }

    @Test
    public void postProduct_success() {
        // given
        PostProductRequestDTO postProductRequestDto = PostProductRequestDTO.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .productOptions(List.of(
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("옵션1")
                                .optionStock(10)
                                .optionPrice(1000)
                                .build(),
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId())
                .brandId(brand.getId())
                .build();

        // when
        Product savedProduct = productService.postProduct(postProductRequestDto, seller);

        // then
        assertNotNull(savedProduct);
        assertEquals(postProductRequestDto.getName(), savedProduct.getName());
        assertEquals(postProductRequestDto.getDescription(), savedProduct.getDescription());
        assertEquals(2, savedProduct.getProductOptions().size());
        assertEquals(brand.getId(), savedProduct.getBrand().getId());
    }

    @Test
    public void patchProduct_success() {
        // given
        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .brand(brand)
                .build();
        productRepository.save(product); // Product 저장

        PostProductRequestDTO postProductRequestDto = PostProductRequestDTO.builder()
                .name("업데이트된 제품")
                .description("업데이트된 설명")
                .productOptions(List.of(
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("업데이트된 옵션")
                                .optionStock(100)
                                .optionPrice(2000)
                                .build(),
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId())
                .brandId(brand.getId())
                .build();

        // when
        Product updatedProduct = productService.patchProduct(postProductRequestDto, seller, product.getId());

        // then
        assertNotNull(updatedProduct);
        assertEquals(postProductRequestDto.getName(), updatedProduct.getName());
        assertEquals(postProductRequestDto.getDescription(), updatedProduct.getDescription());
        assertEquals(brand.getId(), updatedProduct.getBrand().getId());
    }

    @Test
    public void postProductImages_success() {
        // given
        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .brand(brand)
                .build();
        productRepository.save(product); // Product 저장

        MockMultipartFile file1 = new MockMultipartFile("file", "product1.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product2.jpg", "image/jpeg", "test image content".getBytes());

        // Mock 설정
        Mockito.lenient().when(imageStore.createStoreFileName("product1.jpg")).thenReturn("stored-product1.jpg");
        Mockito.lenient().when(imageStore.createStoreFileName("product2.jpg")).thenReturn("stored-product2.jpg");

        // when
        productService.postProductImages(List.of(file1, file2), product, Section.PRODUCT_IMAGE);

        // then
        verify(productImageRepository, times(2)).save(any(ProductImage.class)); // 두 개의 이미지가 저장되는지 확인
    }

    @Test
    public void patchProductImages_success() {
        // given
        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .brand(brand)
                .build();
        productRepository.save(product); // Product 저장

        ProductImage productImage1 = ProductImage.builder()
                .path("aws/s3/path/old_image1.jpg")
                .originalImageName("old_image1.jpg")
                .section(Section.PRODUCT_IMAGE)
                .build();
        ProductImage productImage2 = ProductImage.builder()
                .path("aws/s3/path/old_image2.jpg")
                .originalImageName("old_image2.jpg")
                .section(Section.PRODUCT_DETAIL_IMAGE)
                .build();

        MockMultipartFile newFile1 = new MockMultipartFile("file", "new_product1.jpg", "image/jpeg", "new image content".getBytes());
        MockMultipartFile newFile2 = new MockMultipartFile("file", "new_product2.jpg", "image/jpeg", "new image content".getBytes());

        // Mock 설정
        Mockito.when(productImageRepository.findProductImageByProduct_Id(product.getId()))
                .thenReturn(List.of(productImage1, productImage2));

        // S3에서 이미지 삭제 Mock 설정
        doNothing().when(s3Service).deleteFile(anyString());

        // when
        productService.patchProductImages(List.of(newFile1, newFile2), product, Section.PRODUCT_IMAGE);

        // then
        verify(s3Service, times(1)).deleteFile("aws/s3/path/old_image1.jpg");
        verify(s3Service, times(1)).deleteFile("aws/s3/path/old_image2.jpg");
        verify(productImageRepository, times(1)).deleteProductImageByProduct_Id(product.getId());
        verify(productImageRepository, times(2)).save(any(ProductImage.class)); // 새 이미지가 저장되는지 확인
    }





    @Test
    public void patchProductAuthority_success() {
        // given
        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .brand(brand)
                .build();
        productRepository.save(product); // Product 저장

        // when
        productService.changeProductAuthority(product.getId(), AuthorityStatus.APPROVED);

        // then
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertEquals(AuthorityStatus.APPROVED, updatedProduct.getAuthorityStatus());
    }

    @Test
    public void changeProductAuthority_productNotFound_fail() {
        // when & then
        assertThrows(BaseException.class, () -> {
            productService.changeProductAuthority(999L, AuthorityStatus.APPROVED);
        });
    }
}

