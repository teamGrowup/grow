package org.boot.growup.source.seller.service;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.ProductRequestDTO;
import org.boot.growup.source.seller.persist.entity.MainCategory;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductOption;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.entity.SubCategory;
import org.boot.growup.source.seller.persist.repository.MainCategoryRepository;
import org.boot.growup.source.seller.persist.repository.ProductRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.persist.repository.SubCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
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

    @Test
    public void registerProduct_success() {
        // given
        Seller seller = Seller.builder()
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

        MainCategory mainCategory = MainCategory.builder()
                .name("상의")
                .build();

        mainCategoryRepository.save(mainCategory); // MainCategory 저장

        SubCategory subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory) // MainCategory 설정
                .build();

        subCategoryRepository.save(subCategory); // SubCategory 저장

        ProductRequestDTO productRequestDto = ProductRequestDTO.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .productOptions(List.of(
                        ProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션1")
                                .optionStock(10)
                                .optionPrice(1000)
                                .build(),
                        ProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId()) // SubCategory ID 설정
                .build();

        // when
        Product savedProduct = productService.postProduct(postProductRequestDto, seller);

        // then
        assertNotNull(savedProduct);
        assertEquals(productRequestDto.getName(), savedProduct.getName());
        assertEquals(productRequestDto.getDescription(), savedProduct.getDescription());
        assertEquals(2, savedProduct.getProductOptions().size()); // 옵션 수 확인
    }

    @Test
    public void updateProduct_success() {
        // given
        Seller seller = Seller.builder()
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

        MainCategory mainCategory = MainCategory.builder()
                .name("상의")
                .build();

        mainCategoryRepository.save(mainCategory); // MainCategory 저장

        SubCategory subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory) // MainCategory 설정
                .build();

        subCategoryRepository.save(subCategory); // SubCategory 저장

        Product product = Product.builder()
                .name("테스트 제품")
                .description("테스트 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .build();

        productRepository.save(product); // Product 저장

        ProductRequestDTO productRequestDto = ProductRequestDTO.builder()
                .name("업데이트된 제품")
                .description("업데이트된 설명")
                .productOptions(List.of(
                        ProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션1")
                                .optionStock(10)
                                .optionPrice(1000)
                                .build(),
                        ProductRequestDTO.ProductOptionDTO.builder() // 빌더 사용
                                .optionName("옵션2")
                                .optionStock(5)
                                .optionPrice(1500)
                                .build()
                ))
                .subCategoryId(subCategory.getId()) // SubCategory ID 설정
                .build();

        // when
        Product updatedProduct = productService.updateProduct(productRequestDto, seller);

        // then
        assertNotNull(updatedProduct);
        assertEquals(productRequestDto.getName(), updatedProduct.getName());
        assertEquals(productRequestDto.getDescription(), updatedProduct.getDescription());
    }
}
