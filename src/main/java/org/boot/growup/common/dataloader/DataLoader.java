package org.boot.growup.common.dataloader;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.common.enumerate.Role;
import org.boot.growup.source.seller.persist.entity.*;
import org.boot.growup.source.seller.persist.repository.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader {

    private final BrandRepository brandRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final MainCategoryRepository mainCategoryRepository;
    private final SubCategoryRepository subCategoryRepository;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void init() {
        sellerInit();
        brandInit();
        categoryInit();
        productInit();
    }

    public void brandInit(){
        Brand brand1 = Brand.builder()
                .name("브랜드1")
                .description("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(10)
                .seller(sellerRepository.findById(1L).get())
                .build();

        Brand brand2 = Brand.builder()
                .name("브랜드2")
                .description("브랜드2는 혁신적인 기술과 전통적인 장인 정신을 결합하여 특별한 제품을 만듭니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(20)
                .seller(sellerRepository.findById(2L).get())
                .build();

        Brand brand3 = Brand.builder()
                .name("브랜드3")
                .description("브랜드3은 자연에서 영감을 받은 컬렉션을 통해 지속 가능한 패션을 제안합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(15)
                .build();

        Brand brand4 = Brand.builder()
                .name("브랜드4")
                .description("브랜드4는 다양한 사람들이 함께 즐길 수 있는 편안하고 실용적인 제품을 전개합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(5)
                .build();

        Brand brand5 = Brand.builder()
                .name("브랜드5")
                .description("브랜드5는 세련된 디자인과 높은 품질로 일상에 특별함을 더합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(30)
                .build();

        Brand brand6 = Brand.builder()
                .name("브랜드6")
                .description("브랜드6은 정교한 디테일과 현대적인 감각을 반영한 패션 아이템을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(25)
                .build();

        Brand brand7 = Brand.builder()
                .name("브랜드7")
                .description("브랜드7은 글로벌 트렌드를 반영하여 다양한 스타일의 제품을 선보입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(12)
                .build();

        Brand brand8 = Brand.builder()
                .name("브랜드8")
                .description("브랜드8은 고유의 개성과 독창성을 바탕으로 개성 넘치는 제품을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(18)
                .build();

        Brand brand9 = Brand.builder()
                .name("브랜드9")
                .description("브랜드9은 실용성과 미적 감각을 동시에 만족시키는 제품을 제작합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(22)
                .build();

        Brand brand10 = Brand.builder()
                .name("브랜드10")
                .description("브랜드10은 기능성과 스타일을 모두 갖춘 현대적인 패션 아이템을 제공합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(8)
                .build();
        brandRepository.saveAll(List.of(brand1, brand2
                , brand3, brand4
                , brand5, brand6
                , brand7, brand8
                , brand9, brand10
        ));
    }
    public void sellerInit(){
        Seller seller = Seller.builder()
                .cpEmail("lafudgestore@naver.com")
                .cpPassword("password1234")
                .phoneNumber("010-7797-8841") // 대표 전화번호
                .epName("손준호") // 대표자명
                .cpName("(주)슬로우스탠다드") // 상호명
                .cpCode("178-86-01188") // 10자리의 사업자 등록번호
                .cpAddress("경기도 의정부시 오목로225번길 94, 씨와이파크 (민락동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .role(Role.SELLER)
                .build();
        sellerRepository.save(seller);

        Seller seller2 = Seller.builder()
                .cpEmail("drawfit@naver.com")
                .cpPassword("password1234")
                .phoneNumber("02-3394-8271") // 대표 전화번호
                .epName("조현민") // 대표자명
                .cpName("디알에프티 주식회사") // 상호명
                .cpCode("722-87-00697") // 10자리의 사업자 등록번호
                .cpAddress("서울특별시 성동구 자동차시장1길 81, FCN빌딩 5층 (용답동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .role(Role.CUSTOMER)
                .build();

        sellerRepository.save(seller2);
    }

    public void categoryInit() {
        // 메인 카테고리 초기화
        MainCategory mainCategory1 = MainCategory.builder()
                .name("상의")
                .build();

        MainCategory mainCategory2 = MainCategory.builder()
                .name("바지")
                .build();

        MainCategory mainCategory3 = MainCategory.builder()
                .name("아우터")
                .build();

        MainCategory mainCategory4 = MainCategory.builder()
                .name("신발")
                .build();

        mainCategoryRepository.saveAll(List.of(
                mainCategory1, mainCategory2,
                mainCategory3, mainCategory4));

        SubCategory subCategory1 = SubCategory.builder()
                .name("맨투맨")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory2 = SubCategory.builder()
                .name("반팔")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory3 = SubCategory.builder()
                .name("숏 팬츠")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory4 = SubCategory.builder()
                .name("코튼 팬츠")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory5 = SubCategory.builder()
                .name("후드 집업")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory6 = SubCategory.builder()
                .name("가디건")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory7 = SubCategory.builder()
                .name("스니커즈")
                .mainCategory(mainCategory1)
                .build();

        SubCategory subCategory8 = SubCategory.builder()
                .name("샌들/슬리퍼")
                .mainCategory(mainCategory1)
                .build();

        subCategoryRepository.saveAll(List.of(
                subCategory1, subCategory2,
                subCategory3, subCategory4,
                subCategory5, subCategory6,
                subCategory7, subCategory8
        ));

    }
    public void productInit() {
        // 판매자 및 브랜드 가져오기
        Seller seller1 = sellerRepository.findById(1L).orElseThrow();
        Seller seller2 = sellerRepository.findById(2L).orElseThrow();
        Brand brand1 = brandRepository.findById(1L).orElseThrow();
        Brand brand2 = brandRepository.findById(2L).orElseThrow();

        // 서브 카테고리 가져오기 (ID를 알고 있다고 가정)
        SubCategory subCategory1 = subCategoryRepository.findById(1L).orElseThrow();
        SubCategory subCategory2 = subCategoryRepository.findById(2L).orElseThrow();
        SubCategory subCategory3 = subCategoryRepository.findById(3L).orElseThrow();
        SubCategory subCategory4 = subCategoryRepository.findById(4L).orElseThrow();
        SubCategory subCategory5 = subCategoryRepository.findById(5L).orElseThrow();
        SubCategory subCategory6 = subCategoryRepository.findById(6L).orElseThrow();
        SubCategory subCategory7 = subCategoryRepository.findById(7L).orElseThrow();
        SubCategory subCategory8 = subCategoryRepository.findById(8L).orElseThrow();

        // 제품 초기화
        Product product1 = Product.builder()
                .name("맨투맨 A")
                .description("편안한 맨투맨입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller1)
                .brand(brand1)
                .subCategory(subCategory1) // 맨투맨
                .build();

        Product product2 = Product.builder()
                .name("반팔 티셔츠 B")
                .description("시원한 반팔 티셔츠입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller2)
                .brand(brand2)
                .subCategory(subCategory2) // 반팔
                .build();

        Product product3 = Product.builder()
                .name("숏 팬츠 C")
                .description("여름에 딱인 숏 팬츠입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller1)
                .brand(brand1)
                .subCategory(subCategory3) // 숏 팬츠
                .build();

        Product product4 = Product.builder()
                .name("코튼 팬츠 D")
                .description("편안한 코튼 팬츠입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller2)
                .brand(brand2)
                .subCategory(subCategory4) // 코튼 팬츠
                .build();

        Product product5 = Product.builder()
                .name("후드 집업 E")
                .description("따뜻한 후드 집업입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller1)
                .brand(brand1)
                .subCategory(subCategory5) // 후드 집업
                .build();

        Product product6 = Product.builder()
                .name("가디건 F")
                .description("가벼운 가디건입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller2)
                .brand(brand2)
                .subCategory(subCategory6) // 가디건
                .build();

        Product product7 = Product.builder()
                .name("스니커즈 G")
                .description("스타일리시한 스니커즈입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller1)
                .brand(brand1)
                .subCategory(subCategory7) // 스니커즈
                .build();

        Product product8 = Product.builder()
                .name("샌들/슬리퍼 H")
                .description("여름에 편한 샌들입니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .averageRating(0.0)
                .likeCount(0)
                .seller(seller2)
                .brand(brand2)
                .subCategory(subCategory8) // 샌들/슬리퍼
                .build();

        // 제품 저장
        productRepository.saveAll(List.of(
                product1, product2, product3, product4,
                product5, product6, product7, product8
        ));
    }
}

