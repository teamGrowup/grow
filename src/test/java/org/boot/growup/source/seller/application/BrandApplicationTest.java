package org.boot.growup.source.seller.application;

import org.boot.growup.source.seller.constant.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.RegisterBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.ReadSellerBrandResponseDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.service.BrandImageService;
import org.boot.growup.source.seller.service.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandApplicationTest {


    @InjectMocks
    private BrandApplication brandApplication;

    @Mock
    private BrandServiceImpl brandService;

    @Mock
    private BrandImageService brandImageService;

    @Mock
    private SellerRepository sellerRepository;

    RegisterBrandRequestDTO registerBrandRequestDTO1;
    RegisterBrandRequestDTO registerBrandRequestDTO2;

    Brand brand1;
    BrandImage brandImage1;
    BrandImage brandImage2;
    Seller seller;


    @BeforeEach
    void setUp() {
        registerBrandRequestDTO1 = RegisterBrandRequestDTO.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .build();
        registerBrandRequestDTO2 = RegisterBrandRequestDTO.builder()
                .name("드로우핏")
                .description("드로우핏(DRAW FIT)은 핏과 착용감을 중요하게 여기며 컨템포러리&모던 룩을 지향하는 브랜드입니다. 뛰어난 퀄리티의 좋은 소재를 다양하게 구성해 바느질까지 세심하게 신경 써서 완성하고 고유의 핏과 무드를 머금은 웨어러블한 디자인의 아이템을 합리적인 가격으로 소개합니다.")
                .build();

        brand1 = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likes(0)
                .build();

        brandImage1 = BrandImage.builder()
                .path("aws/s3/path/qyrwqirakfasfk1.jpg")
                .originalImageName("lafudge1.jpg")
                .build();
        brandImage2 = BrandImage.builder()
                .path("aws/s3/path/qyrwqirakfasfk2.jpg")
                .originalImageName("lafudge2.jpg")
                .build();
        seller = Seller.builder()
                .cpEmail("lafudgestore@naver.com")
                .cpPassword("password1234")
                .phoneNumber("010-7797-8841") // 대표 전화번호
                .epName("손준호") // 대표자명
                .cpName("(주)슬로우스탠다드") // 상호명
                .cpCode("178-86-01188") // 10자리의 사업자 등록번호
                .cpAddress("경기도 의정부시 오목로225번길 94, 씨와이파크 (민락동)") // 사업장 소재지(회사주소)
                .netProceeds(1000)
                .build();
    }

    @Test
    public void registerBrandWithBrandImages_Default_Success() {
        //given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());
        given(brandService.registerBrand(registerBrandRequestDTO1, seller)).willReturn(brand1);
        given(sellerRepository.findById(1L)).willReturn(Optional.of(seller));
        List<MultipartFile> mockFiles = List.of(file1, file2);

        //when
        brandApplication.registerBrandWithBrandImages(registerBrandRequestDTO1, mockFiles);

        //then
        verify(brandService).registerBrand(registerBrandRequestDTO1, seller);
        verify(brandImageService).saveBrandImages(mockFiles, brand1);
    }

    @Test
    public void readSellerBrand_Default_Success() {
        //given
        List<BrandImage> brandImages = List.of(brandImage1, brandImage2);
        given(brandService.readBrandBySellerId(1L)).willReturn(brand1);
        given(brandImageService.readBrandImages(brand1.getId())).willReturn(brandImages);
        ReadSellerBrandResponseDTO dto = ReadSellerBrandResponseDTO.builder()
                .name(brand1.getName())
                .description(brand1.getDescription())
                .brandImages(
                        brandImages.stream().map(ReadSellerBrandResponseDTO.BrandImageDTO::from).toList()
                )
                .build();


        //when
        ReadSellerBrandResponseDTO res = brandApplication.readSellerBrand();

        //then
        assertNotNull(res);
        assertEquals(dto, res);
    }

    @Test
    public void updateBrand_Default_Success() {
        //given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());
        given(brandService.updateBrand(registerBrandRequestDTO2, seller)).willReturn(brand1);
        given(sellerRepository.findById(1L)).willReturn(Optional.of(seller));
        List<MultipartFile> mockFiles = List.of(file1, file2);

        //when
        brandApplication.updateBrand(registerBrandRequestDTO2, mockFiles);

        //then
        verify(brandService).updateBrand(registerBrandRequestDTO2, seller);
        verify(brandImageService).updateBrandImages(mockFiles, brand1);
    }
}