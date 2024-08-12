package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ProductImageRepositoryTest {

    @Autowired
    private ProductImageRepository productImageRepository;

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private MainCategoryRepository mainCategoryRepository;
    @Autowired
    private SubCategoryRepository subCategoryRepository;

    private Product product;
    private SubCategory subCategory;
    private MainCategory mainCategory;
    private Seller seller;
    private ProductImage productImage1;
    private ProductImage productImage2;
    private ProductImage productImage3;

    @BeforeEach
    void setUp() {

        productImageRepository.deleteAll();
        productRepository.deleteAll();
        subCategoryRepository.deleteAll();
        mainCategoryRepository.deleteAll();
        sellerRepository.deleteAll();

        mainCategory = MainCategory.builder()
                .name("상의")
                .build();
        mainCategoryRepository.save(mainCategory);

        subCategory = SubCategory.builder()
                .name("반팔")
                .mainCategory(mainCategory)
                .build();
        subCategoryRepository.save(subCategory);

        // Seller 저장
        seller = Seller.builder()
                .cpAddress("서울특별시 강남구") // cp_address 설정
                .cpCode("ABC123") // cp_code 설정
                .cpEmail("test@seller.com") // cp_email 설정
                .cpName("테스트 연락처") // cp_name 설정
                .cpPassword("password") // cp_password 설정
                .epName("테스트 업체") // ep_name 설정
                .netProceeds(100000) // net_proceeds 설정
                .phoneNumber("010-1234-5678") // phone_number 설정
                .build();
        sellerRepository.save(seller); // Seller 저장

        product = Product.builder()
                .name("진짜 반팔")
                .description("순도 100%의 반팔입니다. 긴팔도 아니고 나시도 아니고 진짜 반팔이에요.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .seller(seller)
                .build();


        productRepository.save(product); // Product 저장

        productImage1 = ProductImage.builder()
                .originalImageName("ProductImage1.jpg")
                .path("https://bucket.s3.region.amazonaws.com/ProductImage1.jpg")
                .product(product)
                .section(Section.PRODUCT_IMAGE)
                .build();

        productImage2 = ProductImage.builder()
                .originalImageName("ProductImage2.jpg")
                .path("https://bucket.s3.region.amazonaws.com/ProductImage2.jpg")
                .product(product)
                .section(Section.PRODUCT_DETAIL_IMAGE)
                .build();

        productImage3 = ProductImage.builder()
                .originalImageName("ProductImage3.jpg")
                .path("https://bucket.s3.region.amazonaws.com/ProductImage3.jpg")
                .product(product)
                .section(Section.PRODUCT_IMAGE)
                .build();
    }

    @Test
    public void productImageInsert_Default_Success() {

        // given
        ProductImage productImage = ProductImage.builder()
                .originalImageName("ProductImage1.jpg")
                .path("https://bucket.s3.region.amazonaws.com/ProductImage1.jpg")
                .product(product)
                .section(Section.PRODUCT_IMAGE)
                .build();

        // when
        ProductImage productImageDB = productImageRepository.save(productImage);

        // then
        assertNotNull(productImageDB);
        assertEquals("ProductImage1.jpg", productImageDB.getOriginalImageName());
        assertEquals(productImage.getPath(), productImageDB.getPath());
        assertEquals(product, productImageDB.getProduct());
        assertNotNull(productImageDB.getId()); // ID가 null이 아니어야 함
    }

    @Test
    public void findProductImageByProduct_Id_Default_Success() {
        //given
        productImageRepository.save(productImage1);
        productImageRepository.save(productImage2);
        productImageRepository.save(productImage3);

        // when
        List<ProductImage> productImages = productImageRepository.findProductImageByProduct_Id(product.getId());

        // then
        assertNotNull(productImages);
        assertEquals(3, productImages.size());
    }

    @Test
    public void deleteProductImageByProduct_Id_Default_Success() {
        // given
        productImageRepository.save(productImage1);
        productImageRepository.save(productImage2);
        productImageRepository.save(productImage3);

        // when
        productImageRepository.deleteProductImageByProduct_Id(product.getId());

        // then
        assertEquals(0, productImageRepository.findProductImageByProduct_Id(product.getId()).size());
    }
}
