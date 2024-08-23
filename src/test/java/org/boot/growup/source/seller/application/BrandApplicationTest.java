package org.boot.growup.source.seller.application;

import org.boot.growup.common.constant.AuthorityStatus;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.dto.request.PostBrandRequestDTO;
import org.boot.growup.product.dto.response.GetBrandDetailResponseDTO;
import org.boot.growup.product.dto.response.GetBrandRequestByStatusResponseDTO;
import org.boot.growup.product.dto.response.GetSellerBrandResponseDTO;
import org.boot.growup.product.persist.entity.Brand;
import org.boot.growup.product.persist.entity.BrandImage;
import org.boot.growup.product.service.BrandService;
import org.boot.growup.auth.persist.entity.Seller;
import org.boot.growup.auth.service.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

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
    private BrandService brandService;

    @Mock
    private SellerService sellerService;

    private PostBrandRequestDTO postBrandRequestDTO;
    private Brand brand;
    private Seller seller;
    private BrandImage brandImage1;
    private BrandImage brandImage2;

    @BeforeEach
    void setUp() {
        postBrandRequestDTO = PostBrandRequestDTO.builder()
                .name("테스트 브랜드")
                .description("테스트 브랜드 설명")
                .build();

        brand = Brand.builder()
                .name("테스트 브랜드")
                .description("테스트 브랜드 설명")
                .authorityStatus(AuthorityStatus.PENDING)
                .likeCount(0)
                .build();

        brandImage1 = BrandImage.builder()
                .path("aws/s3/path/image1.jpg")
                .originalImageName("image1.jpg")
                .build();

        brandImage2 = BrandImage.builder()
                .path("aws/s3/path/image2.jpg")
                .originalImageName("image2.jpg")
                .build();

        seller = Seller.builder()
                .cpEmail("test@example.com")
                .cpPassword("password")
                .phoneNumber("010-1234-5678")
                .epName("테스트 판매자")
                .build();
    }

    @Test
    public void postBrandWithBrandImages_Default_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(brandService.postBrand(postBrandRequestDTO, seller)).willReturn(brand);

        List<MultipartFile> mockFiles = List.of(file1, file2);

        // when
        brandApplication.postBrandWithBrandImages(postBrandRequestDTO, mockFiles);

        // then
        verify(sellerService).getCurrentSeller();
        verify(brandService).postBrand(postBrandRequestDTO, seller);
        verify(brandService).postBrandImages(mockFiles, brand);
    }

    @Test
    public void getSellerBrand_Default_Success() {
        // given
        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(brandService.getBrandBySellerId(seller.getId())).willReturn(brand);
        given(brandService.getBrandImages(brand.getId())).willReturn(List.of(brandImage1, brandImage2));

        // when
        GetSellerBrandResponseDTO response = brandApplication.getSellerBrand();

        // then
        assertNotNull(response);
        assertEquals(brand.getName(), response.getName());
        assertEquals(brand.getDescription(), response.getDescription());
        assertEquals(2, response.getBrandImages().size());
    }

    @Test
    public void patchBrand_Default_Success() {
        // given
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        given(sellerService.getCurrentSeller()).willReturn(seller);
        given(brandService.patchBrand(postBrandRequestDTO, seller)).willReturn(brand);

        List<MultipartFile> mockFiles = List.of(file1, file2);

        // when
        brandApplication.patchBrand(postBrandRequestDTO, mockFiles);

        // then
        verify(sellerService).getCurrentSeller();
        verify(brandService).patchBrand(postBrandRequestDTO, seller);
        verify(brandService).patchBrandImages(mockFiles, brand);
    }

    @Test
    public void denyBrandPost_Default_Success() {
        // given
        Long brandId = 1L;

        // when
        brandApplication.denyBrandPost(brandId);

        // then
        verify(brandService).changeBrandAuthority(brandId, AuthorityStatus.DENIED);
    }

    @Test
    public void approveBrandPost_Default_Success() {
        // given
        Long brandId = 1L;

        // when
        brandApplication.approveBrandPost(brandId);

        // then
        verify(brandService).changeBrandAuthority(brandId, AuthorityStatus.APPROVED);
    }

    @Test
    public void pendingBrandRegister_Default_Success() {
        // given
        Long brandId = 1L;

        // when
        brandApplication.pendingBrandRegister(brandId);

        // then
        verify(brandService).changeBrandAuthority(brandId, AuthorityStatus.PENDING);
    }

    @Test
    public void getBrandRequestByStatus_Default_Success() {
        // given
        given(brandService.getBrandRequestsByStatus(AuthorityStatus.PENDING, 0))
                .willReturn(Collections.nCopies(10, brand));

        // when
        var response = brandApplication.getBrandRequestByStatus(AuthorityStatus.PENDING, 0);

        // then
        assertEquals(10, response.size());
        assertEquals(GetBrandRequestByStatusResponseDTO.class, response.get(0).getClass());
    }

    @Test
    public void getBrandRequestByStatus_NoStatus_Success() {
        // given
        given(brandService.getBrandRequestsByStatus(null, 0))
                .willReturn(Collections.nCopies(10, brand));

        // when
        var response = brandApplication.getBrandRequestByStatus(null, 0);

        // then
        assertEquals(10, response.size());
        assertEquals(GetBrandRequestByStatusResponseDTO.class, response.get(0).getClass());
    }

    @Test
    public void getBrandDetail_Default_Success() {
        // given
        Long brandId = 1L;
        given(brandService.getBrandById(brandId)).willReturn(brand);
        given(brandService.getBrandImages(brand.getId())).willReturn(List.of(brandImage1, brandImage2));

        // when
        GetBrandDetailResponseDTO response = brandApplication.getBrandDetail(brandId);

        // then
        assertNotNull(response);
        assertEquals(brand.getName(), response.getName());
        assertEquals(brand.getDescription(), response.getDescription());
        assertEquals(brand.getLikeCount(), response.getLikeCount());
        assertEquals(brandImage1.getOriginalImageName(), response.getImages().get(0).getOriginalImageName());
        assertEquals(brandImage1.getPath(), response.getImages().get(0).getPath());
        assertEquals(brandImage2.getOriginalImageName(), response.getImages().get(1).getOriginalImageName());
        assertEquals(brandImage2.getPath(), response.getImages().get(1).getPath());
    }
}