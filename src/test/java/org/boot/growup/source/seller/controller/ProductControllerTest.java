package org.boot.growup.source.seller.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.boot.growup.common.constant.Section;
import org.boot.growup.auth.utils.JwtAuthenticationFilter;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.product.application.ProductApplication;
import org.boot.growup.product.controller.ProductController;
import org.boot.growup.product.dto.request.PostProductRequestDTO;
import org.boot.growup.product.dto.response.GetProductDetailResponseDTO;
import org.boot.growup.common.constant.AuthorityStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = ProductController.class)
@WithMockUser
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductApplication productApplication;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void postProduct_Success() throws Exception {
        // given
        PostProductRequestDTO requestDto = PostProductRequestDTO.builder()
                .name("테스트 제품")
                .description("테스트 제품 설명")
                .subCategoryId(1L)
                .brandId(1L)
                .productOptions(List.of(
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("옵션1")
                                .optionStock(10)
                                .optionPrice(1000)
                                .build()
                ))
                .build();

        // 메서드가 호출되는지 확인하기 위해 void로 설정
        doNothing().when(productApplication).postProductWithImages(any(), any());

        // when, then
        mockMvc.perform(multipart("/sellers/products")
                        .file("images", "test image content".getBytes()) // 이미지 파일 시뮬레이션
                        .param("form", new ObjectMapper().writeValueAsString(requestDto))
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .with(csrf())) // CSRF 토큰 추가
                .andExpect(status().isOk())
                //.andExpect(content().contentType(MediaType.APPLICATION_JSON)) // JSON 형식 확인
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(jsonPath("$.message", Matchers.is("등록 성공")))
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true)));

        // 메서드가 호출되었는지 확인
        verify(productApplication).postProductWithImages(any(), any());
    }



    @Test
    public void getProductDetail_Success() throws Exception {
        // given
        List<GetProductDetailResponseDTO.ProductImageDTO> productImages = List.of(
                GetProductDetailResponseDTO.ProductImageDTO.builder()
                        .path("aws/s3/path/to/image1.jpg")
                        .originalImageName("image1.jpg")
                        .section(Section.PRODUCT_IMAGE) // 적절한 Section 값 설정
                        .build(),
                GetProductDetailResponseDTO.ProductImageDTO.builder()
                        .path("aws/s3/path/to/image2.jpg")
                        .originalImageName("image2.jpg")
                        .section(Section.PRODUCT_DETAIL_IMAGE) // 적절한 Section 값 설정
                        .build()
        );

        List<GetProductDetailResponseDTO.ProductOptionDTO> productOptions = List.of(
                GetProductDetailResponseDTO.ProductOptionDTO.builder()
                        .optionName("옵션1")
                        .optionStock(10)
                        .optionPrice(1000)
                        .build()
        );

        GetProductDetailResponseDTO responseDto = GetProductDetailResponseDTO.builder()
                .productId(1L)
                .name("테스트 제품")
                .description("테스트 제품 설명")
                .averageRating(4.5)
                .likeCount(100)
                .authorityStatus(AuthorityStatus.APPROVED) // 적절한 AuthorityStatus 값 설정
                .subCategoryId(1L)
                .mainCategoryId(1L)
                .productImages(productImages)
                .productOptions(productOptions)
                .build();

        given(productApplication.getProductDetail(1L)).willReturn(responseDto);

        // when, then
        mockMvc.perform(get("/sellers/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(jsonPath("$.message", Matchers.is("요청에 성공하였습니다.")))
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true)))
                .andExpect(jsonPath("$.data.productId", Matchers.is(1)))
                .andExpect(jsonPath("$.data.name", Matchers.is("테스트 제품")))
                .andExpect(jsonPath("$.data.description", Matchers.is("테스트 제품 설명")))
                .andExpect(jsonPath("$.data.averageRating", Matchers.is(4.5)))
                .andExpect(jsonPath("$.data.likeCount", Matchers.is(100)))
                .andExpect(jsonPath("$.data.authorityStatus", Matchers.is("APPROVED")))
                .andExpect(jsonPath("$.data.subCategoryId", Matchers.is(1)))
                .andExpect(jsonPath("$.data.mainCategoryId", Matchers.is(1)))
                .andExpect(jsonPath("$.data.productImages[0].path", Matchers.is("aws/s3/path/to/image1.jpg")))
                .andExpect(jsonPath("$.data.productImages[0].originalImageName", Matchers.is("image1.jpg")))
                .andExpect(jsonPath("$.data.productOptions[0].optionName", Matchers.is("옵션1")))
                .andExpect(jsonPath("$.data.productOptions[0].optionStock", Matchers.is(10)))
                .andExpect(jsonPath("$.data.productOptions[0].optionPrice", Matchers.is(1000)));
    }

    @Test
    public void patchProduct_Success() throws Exception {
        // given
        PostProductRequestDTO requestDto = PostProductRequestDTO.builder()
                .name("업데이트된 제품")
                .description("업데이트된 설명")
                .subCategoryId(1L)
                .brandId(1L)
                .productOptions(List.of(
                        PostProductRequestDTO.ProductOptionDTO.builder()
                                .optionName("업데이트된 옵션")
                                .optionStock(100)
                                .optionPrice(2000)
                                .build()
                ))
                .build();

        // when, then
        mockMvc.perform(multipart("/sellers/products/{productId}", 1L)
                        .file("images", "updated image content".getBytes()) // 업데이트된 이미지 파일 시뮬레이션
                        .param("form", new ObjectMapper().writeValueAsString(requestDto))
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(jsonPath("$.message", Matchers.is("업데이트 성공")))
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true)));
    }

    @Test
    public void deleteProduct_Success() throws Exception {
        // given
        doNothing().when(productApplication).deleteProduct(1L);

        // when, then
        mockMvc.perform(delete("/sellers/products/{productId}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(jsonPath("$.message", Matchers.is("삭제 성공")))
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true)));
    }
}