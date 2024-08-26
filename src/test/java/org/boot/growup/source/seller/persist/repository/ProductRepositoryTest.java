package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.auth.persist.repository.SellerRepository;
import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.common.constant.Role;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.product.persist.repository.BrandRepository;
import org.boot.growup.product.persist.repository.MainCategoryRepository;
import org.boot.growup.product.persist.repository.ProductRepository;
import org.boot.growup.product.persist.repository.SubCategoryRepository;
import org.junit.jupiter.api.BeforeEach;
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

    @Autowired
    private BrandRepository brandRepository;

    private PostProductRequestDTO postProductRequestDTO;
    private Seller seller;
    private SubCategory subCategory;
    private MainCategory mainCategory;
    private Brand brand;

    @BeforeEach
    void setUp() {
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
        mainCategoryRepository.save(mainCategory);

        // SubCategory 초기화
        subCategory = SubCategory.builder()
                .name("반팔")
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

        brand = brandRepository.save(brand); // Brand 저장
    }

    @Test
    public void save_success() {
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

        // when
        Optional<Product> foundProduct = productRepository.findById(product.getId());

        // then
        assertTrue(foundProduct.isPresent());
        assertEquals(product.getName(), foundProduct.get().getName());
    }
}
