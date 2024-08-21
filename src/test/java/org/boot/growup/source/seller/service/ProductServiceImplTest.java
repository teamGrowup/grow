package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.PostProductRequestDTO;

import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.*;
import org.boot.growup.source.seller.service.Impl.ProductServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
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

    private Seller seller;
    private MainCategory mainCategory;
    private SubCategory subCategory;
    private Brand brand;

    @BeforeEach
    public void setup() {
        seller = Seller.builder()
                .cpEmail("test@seller.com")
                .cpPassword("password")
                .phoneNumber("010-1234-5678")
                .epName("테스트 업체")
                .cpName("테스트 연락처")
                .cpCode("123-45-67890")
                .cpAddress("테스트 주소")
                .netProceeds(1000)
                .build();
        sellerRepository.save(seller); // Seller 저장

        mainCategory = MainCategory.builder()
                .name("상의")
                .build();
        mainCategoryRepository.save(mainCategory); // MainCategory 저장

        subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory) // MainCategory 설정
                .build();
        subCategoryRepository.save(subCategory); // SubCategory 저장

        brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .seller(seller)
                .build();
        brandRepository.save(brand); // Brand 저장
    }

    @Test
    public void registerProduct_withBrand_success() {
        // given
        PostProductRequestDTO postProductRequestDto = PostProductRequestDTO.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .productOptions(List.of(
                        PostProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션1")
                                .optionStock(10)
                                .optionPrice(1000)
                                .build(),
                        PostProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId()) // SubCategory ID 설정
                .brandId(brand.getId()) // Brand ID 설정
                .build();

        // when
        Product savedProduct = productService.postProduct(postProductRequestDto, seller);

        // then
        assertNotNull(savedProduct);
        assertEquals(postProductRequestDto.getName(), savedProduct.getName());
        assertEquals(postProductRequestDto.getDescription(), savedProduct.getDescription());
        assertEquals(2, savedProduct.getProductOptions().size()); // 옵션 수 확인
        assertEquals(brand.getId(), savedProduct.getBrand().getId()); // 브랜드 확인
    }

    @Test
    public void patchProduct_withBrand_success() {
        // given
        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .brand(brand) // 브랜드 설정
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
                        PostProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId()) // SubCategory ID 설정
                .brandId(brand.getId()) // Brand ID 설정
                .build();

        // when
        Product updatedProduct = productService.patchProduct(postProductRequestDto, seller, product.getId());

        // then
        assertNotNull(updatedProduct);
        assertEquals(postProductRequestDto.getName(), updatedProduct.getName());
        assertEquals(postProductRequestDto.getDescription(), updatedProduct.getDescription());
        assertEquals(brand.getId(), updatedProduct.getBrand().getId()); // 브랜드 확인
    }
}
