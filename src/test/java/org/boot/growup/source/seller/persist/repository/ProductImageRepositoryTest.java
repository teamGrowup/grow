package org.boot.growup.source.seller.persist.repository;

import org.boot.growup.auth.persist.repository.SellerRepository;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.common.constant.Role;
import org.boot.growup.common.constant.Section;
import org.boot.growup.product.persist.entity.*;
import org.boot.growup.product.persist.repository.*;
import org.boot.growup.common.constant.AuthorityStatus;
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

    @Autowired
    private BrandRepository brandRepository;

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
                .role(Role.SELLER)
                .build();
        sellerRepository.save(seller); // Seller 저장

        Brand brand = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .seller(seller)
                .build();
        brandRepository.save(brand);

        product = Product.builder()
                .name("진짜 반팔")
                .description("순도 100%의 반팔입니다. 긴팔도 아니고 나시도 아니고 진짜 반팔이에요.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .subCategory(subCategory)
                .brand(brand)
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
