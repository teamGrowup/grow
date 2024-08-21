package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.auth.persist.repository.SellerRepository;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.persist.entity.MainCategory;
import org.boot.growup.product.persist.entity.Product;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.product.persist.entity.SubCategory;
import org.boot.growup.product.persist.repository.MainCategoryRepository;
import org.boot.growup.product.persist.repository.ProductRepository;
import org.boot.growup.product.persist.repository.SubCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Autowired
    private SubCategoryRepository subCategoryRepository;

    @Autowired
    private MainCategoryRepository mainCategoryRepository;

    @Test
    public void save_success() {
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

        mainCategoryRepository.save(mainCategory);

        SubCategory subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory) // MainCategory가 필요하다면 추가하세요
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

        // when
        Product productDB = productRepository.save(product);

        // then
        assertNotNull(productDB);
        assertEquals(product.getName(), productDB.getName());
        assertEquals(product.getDescription(), productDB.getDescription());
        assertEquals(0, productDB.getLikeCount());
    }

    @Test
    public void findById_success() {
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

        mainCategoryRepository.save(mainCategory);

        SubCategory subCategory = SubCategory.builder()
                .name("테스트 카테고리")
                .mainCategory(mainCategory) // MainCategory가 필요하다면 추가하세요
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

        // when
        Optional<Product> foundProduct = productRepository.findById(product.getId());

        // then
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getName(), foundProduct.get().getName());
    }

}
