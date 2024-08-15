package org.boot.growup.source.seller.application;

import org.boot.growup.common.enumerate.AuthorityStatus;
import org.boot.growup.source.seller.dto.request.PostBrandRequestDTO;
import org.boot.growup.source.seller.dto.response.GetBrandDetailResponseDTO;
import org.boot.growup.source.seller.dto.response.GetBrandRequestByStatusResponseDTO;
import org.boot.growup.source.seller.dto.response.getSellerBrandResponseDTO;
import org.boot.growup.source.seller.persist.entity.Brand;
import org.boot.growup.source.seller.persist.entity.BrandImage;
import org.boot.growup.source.seller.persist.entity.Seller;
import org.boot.growup.source.seller.persist.repository.BrandRepository;
import org.boot.growup.source.seller.persist.repository.SellerRepository;
import org.boot.growup.source.seller.service.BrandImageService;
import org.boot.growup.source.seller.service.BrandServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
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

    PostBrandRequestDTO postBrandRequestDTO1;
    PostBrandRequestDTO postBrandRequestDTO2;

    Brand brand1;
    BrandImage brandImage1;
    BrandImage brandImage2;
    Seller seller;
    @Autowired
    private BrandRepository brandRepository;

    @BeforeEach
    void setUp() {
        brandRepository.deleteAll();
        postBrandRequestDTO1 = PostBrandRequestDTO.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .build();
        postBrandRequestDTO2 = PostBrandRequestDTO.builder()
                .name("드로우핏")
                .description("드로우핏(DRAW FIT)은 핏과 착용감을 중요하게 여기며 컨템포러리&모던 룩을 지향하는 브랜드입니다. 뛰어난 퀄리티의 좋은 소재를 다양하게 구성해 바느질까지 세심하게 신경 써서 완성하고 고유의 핏과 무드를 머금은 웨어러블한 디자인의 아이템을 합리적인 가격으로 소개합니다.")
                .build();

        brand1 = Brand.builder()
                .name("라퍼지스토어")
                .description("라퍼지스토어(LAFUDGESTORE)는 다양한 사람들이 일상에서 편안하게 사용할 수 있는 제품을 전개합니다. 새롭게 변화되는 소재와 실루엣, 일상에 자연스레 스며드는 제품을 제작하여 지속적인 실속형 소비의 가치를 실천합니다.")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
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
    public void postBrandWithBrandImages_Default_Success() {
        //given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());
        given(brandService.postBrand(postBrandRequestDTO1, seller)).willReturn(brand1);
        given(sellerRepository.findById(1L)).willReturn(Optional.of(seller));
        List<MultipartFile> mockFiles = List.of(file1, file2);

        //when
        brandApplication.postBrandWithBrandImages(postBrandRequestDTO1, mockFiles);

        //then
        verify(brandService).postBrand(postBrandRequestDTO1, seller);
        verify(brandImageService).postBrandImages(mockFiles, brand1);
    }

    @Test
    public void getSellerBrand_Default_Success() {
        //given
        List<BrandImage> brandImages = List.of(brandImage1, brandImage2);
        given(brandService.getBrandBySellerId(1L)).willReturn(brand1);
        given(brandImageService.getBrandImages(brand1.getId())).willReturn(brandImages);
        getSellerBrandResponseDTO dto = getSellerBrandResponseDTO.builder()
                .name(brand1.getName())
                .description(brand1.getDescription())
                .brandImages(
                        brandImages.stream().map(getSellerBrandResponseDTO.BrandImageDTO::from).toList()
                )
                .build();


        //when
        getSellerBrandResponseDTO res = brandApplication.getSellerBrand();

        //then
        assertNotNull(res);
        assertEquals(dto, res);
    }

    @Test
    public void patchBrand_Default_Success() {
        //given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());
        given(brandService.patchBrand(postBrandRequestDTO2, seller)).willReturn(brand1);
        given(sellerRepository.findById(1L)).willReturn(Optional.of(seller));
        List<MultipartFile> mockFiles = List.of(file1, file2);

        //when
        brandApplication.patchBrand(postBrandRequestDTO2, mockFiles);

        //then
        verify(brandService).patchBrand(postBrandRequestDTO2, seller);
        verify(brandImageService).patchBrandImages(mockFiles, brand1);
    }

    @Test
    public void denyBrandPost_Default_Success(){
        //given
        brandRepository.save(brand1);

        //when
        brandApplication.denyBrandPost(1L);

        //then
        verify(brandService).changeBrandAuthority(1L, AuthorityStatus.DENIED);
    }

    @Test
    public void approveBrandPost_Default_Success() {
        //given
        brandRepository.save(brand1);

        //when
        brandApplication.approveBrandPost(1L);

        //then
        verify(brandService).changeBrandAuthority(1L, AuthorityStatus.APPROVED);
    }

    @Test
    public void pendingBrandRegister_Default_Success() {
        //given
        brandRepository.save(brand1);

        //when
        brandApplication.pendingBrandRegister(1L);

        //then
        verify(brandService).changeBrandAuthority(1L, AuthorityStatus.PENDING);

    }

    @Test
    public void getBrandRequestByStatus_Default_Success() {
        //given
        Brand mockBrand = mock(Brand.class);
        given(mockBrand.getId()).willReturn(1L);
        given(mockBrand.getName()).willReturn("Test Brand");
        given(mockBrand.getAuthorityStatus()).willReturn(AuthorityStatus.PENDING);  // 올바른 타입 반환

        given(brandService.getBrandRequestsByStatus(AuthorityStatus.PENDING, 0))
                .willReturn(Collections.nCopies(10, mockBrand));

        //when
        var res = brandApplication.getBrandRequestByStatus(AuthorityStatus.PENDING, 0);

        //then
        assertEquals(10, res.size());
        assertEquals(res.get(0).getClass(), GetBrandRequestByStatusResponseDTO.class);
    }

    @Test
    public void getBrandRequestByStatus_NoStatus_Success() {
        //given
        Brand mockBrand = mock(Brand.class);
        given(mockBrand.getId()).willReturn(1L);
        given(mockBrand.getName()).willReturn("Test Brand");
        given(mockBrand.getAuthorityStatus()).willReturn(AuthorityStatus.PENDING);  // 올바른 타입 반환

        given(brandService.getBrandRequestsByStatus(null, 0))
                .willReturn(Collections.nCopies(10, mockBrand));

        //when
        var res = brandApplication.getBrandRequestByStatus(null, 0);

        //then
        assertEquals(10, res.size());
        assertEquals(res.get(0).getClass(), GetBrandRequestByStatusResponseDTO.class);
    }

    @Test
    public void getBrandDetail_Default_Success() {
        //given
        given(brandService.getBrandById(1L)).willReturn(brand1);
        given(brandImageService.getBrandImages(brand1.getId())).willReturn(List.of(brandImage1, brandImage2));

        //when
        GetBrandDetailResponseDTO res = brandApplication.getBrandDetail(1L);

        //then
        assertNotNull(res);
        assertEquals(brand1.getName(), res.getName());
        assertEquals(brand1.getDescription(), res.getDescription());
        assertEquals(brand1.getLikeCount(), res.getLikeCount());
        assertEquals(brandImage1.getOriginalImageName(), res.getImages().get(0).getOriginalImageName());
        assertEquals(brandImage1.getPath(), res.getImages().get(0).getPath());
        assertEquals(brandImage2.getOriginalImageName(), res.getImages().get(1).getOriginalImageName());
        assertEquals(brandImage2.getPath(), res.getImages().get(1).getPath());
    }


}