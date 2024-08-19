package org.boot.growup.source.seller.service;

import org.boot.growup.common.ImageStore;
import org.boot.growup.common.enumerate.Section;
import org.boot.growup.source.seller.persist.entity.Product;
import org.boot.growup.source.seller.persist.entity.ProductImage;
import org.boot.growup.source.seller.persist.repository.ProductImageRepository;
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
class ProductImageServiceImplTest {
    @InjectMocks
    private ProductImageServiceImpl productImageServiceImpl;

    @Mock
    private ProductImageRepository productImageRepository;

    @Mock
    private ImageStore imageStore;

    private final String testDir = "C:\\Users\\xcv41\\IdeaProjects\\grow\\src\\main\\java\\org\\boot\\growup\\source\\seller";

    @BeforeEach
    public void setUp() {
        File dir = new File(testDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void saveProductImages_Success() {
        // given
        Product product = new Product();
        MockMultipartFile file1 = new MockMultipartFile("file", "product1.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product2.jpg", "image/jpeg", "test image content".getBytes());

        // createStoreFileName 메서드 Mock 설정
        when(imageStore.createStoreFileName("product1.jpg")).thenReturn("stored-product1.jpg");
        when(imageStore.createStoreFileName("product2.jpg")).thenReturn("stored-product2.jpg");

        // when
        productImageServiceImpl.postProductImages(List.of(file1, file2), product, Section.PRODUCT_IMAGE);

        // then
        verify(productImageRepository, times(2)).save(any(ProductImage.class));
    }

    @Test
    public void patchProductImages_Success() {
        //given
        Product product = new Product();
        MockMultipartFile file1 = new MockMultipartFile("file", "product1.jpg", "image/jpeg", "test image content".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("file", "product2.jpg", "image/jpeg", "test image content".getBytes());

        // createStoreFileName 메서드 Mock 설정
        when(imageStore.createStoreFileName("product1.jpg")).thenReturn("stored-product1.jpg");
        when(imageStore.createStoreFileName("product2.jpg")).thenReturn("stored-product2.jpg");


        // when
        productImageServiceImpl.patchProductImages(List.of(file1, file2), product, Section.PRODUCT_IMAGE);

        // then
        verify(productImageRepository, times(1)).deleteProductImageByProduct_Id(product.getId());
        verify(productImageRepository, times(2)).save(any(ProductImage.class));
    }
}
