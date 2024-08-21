package org.boot.growup.source.seller.service;

import org.boot.growup.common.utils.ImageStore;
import org.boot.growup.product.persist.entity.Brand;
import org.boot.growup.product.persist.entity.BrandImage;
import org.boot.growup.product.persist.repository.BrandImageRepository;
import org.boot.growup.product.service.Impl.BrandImageServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BrandImageServiceImplTest {
    @InjectMocks
    private BrandImageServiceImpl brandImageServiceImpl;

    @Mock
    private BrandImageRepository brandImageRepository;

    @Mock
    private ImageStore imageStore;

    @BeforeEach
    public void setUp() {
        File dir = new File("/Users/gnues/Documents/grow/Images/brandImages/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void postBrandImages_Default_Success() {
        // given
        Brand brand = new Brand();

        // 두 개의 MockMultipartFile 객체 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        // Mock된 메소드 동작 설정
        when(imageStore.createStoreFileName("test1.jpg")).thenReturn("uud-test1.jpg");
        when(imageStore.createStoreFileName("test2.jpg")).thenReturn("uud-test2.jpg");

        // when
        brandImageServiceImpl.postBrandImages(List.of(file1, file2), brand);

        // then
        verify(imageStore, times(1)).createStoreFileName("test1.jpg");
        verify(imageStore, times(1)).createStoreFileName("test2.jpg");

        verify(brandImageRepository, times(2)).save(any(BrandImage.class));
    }

    @Test
    public void getBrandImages_Default_Success() {

    }

    @Test
    public void patchBrandImages_Default_Success() {
        // given
        Brand brand = Brand.builder().id(1L).build();

        // 두 개의 MockMultipartFile 객체 생성
        MockMultipartFile file1 = new MockMultipartFile("file", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        // Mock된 메소드 동작 설정
        when(imageStore.createStoreFileName("test1.jpg")).thenReturn("uud-test1.jpg");
        when(imageStore.createStoreFileName("test2.jpg")).thenReturn("uud-test2.jpg");

        // when
        brandImageServiceImpl.patchBrandImages(List.of(file1, file2), brand);

        // then
        verify(imageStore, times(1)).createStoreFileName("test1.jpg");
        verify(imageStore, times(1)).createStoreFileName("test2.jpg");

        verify(brandImageRepository, times(2)).save(any(BrandImage.class));
    }

}