package org.boot.growup.source.seller.controller;

import org.boot.growup.auth.utils.JwtAuthenticationFilter;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.controller.BrandController;
import org.boot.growup.product.dto.request.PostBrandRequestDTO;
import org.boot.growup.product.dto.response.GetSellerBrandResponseDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = BrandController.class)
@WithMockUser(roles = "SELLER")
@MockBean(JpaMetamodelMappingContext.class)
class BrandControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BrandApplication brandApplication;

    @MockBean
    private JwtTokenProvider jwtTokenProvider;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Test
    public void getSellerBrand_Default_Success() throws Exception {
        // given
        GetSellerBrandResponseDTO responseDTO = GetSellerBrandResponseDTO.builder()
                .name("브랜드1")
                .description("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다.")
                .brandImages(List.of(GetSellerBrandResponseDTO.BrandImageDTO.builder()
                        .path("aws/s3/path1/brand1")
                        .originalImageName("brand1.jpg")
                        .build()))
                .build();

        given(brandApplication.getSellerBrand()).willReturn(responseDTO);

        // when, then
        mockMvc.perform(get("/sellers/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(200))) // 응답 코드 확인
                .andExpect(jsonPath("$.message", Matchers.is("요청에 성공하였습니다."))) // 메시지 확인
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true))) // 성공 여부 확인
                .andExpect(jsonPath("$.data.name", Matchers.is("브랜드1"))) // 브랜드 이름 확인
                .andExpect(jsonPath("$.data.description", Matchers.is("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다."))) // 설명 확인
                .andExpect(jsonPath("$.data.brandImages[0].path", Matchers.is("aws/s3/path1/brand1"))) // 이미지 경로 확인
                .andExpect(jsonPath("$.data.brandImages[0].originalImageName", Matchers.is("brand1.jpg"))) // 원본 이미지 이름 확인
                .andDo(MockMvcResultHandlers.print(System.out)); // 응답 출력
    }


    @Test
    public void postBrand_Default_Success() throws Exception {
        // given
        PostBrandRequestDTO postBrandRequestDTO = PostBrandRequestDTO.builder()
                .name("브랜드2")
                .description("새로운 브랜드 설명")
                .build();

        MockMultipartFile file1 = new MockMultipartFile("brandImages", "test1.jpg", "image/jpeg", "test image content 1".getBytes());
        MockMultipartFile file2 = new MockMultipartFile("brandImages", "test2.jpg", "image/jpeg", "test image content 2".getBytes());

        // void 메서드에 대한 설정
        doNothing().when(brandApplication).postBrandWithBrandImages(postBrandRequestDTO, List.of(file1, file2));

        // when
        mockMvc.perform(multipart("/sellers/brands")
                        .file(file1)
                        .file(file2)
                        .param("form", "{\"name\":\"브랜드2\",\"description\":\"새로운 브랜드 설명\"}")
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is("등록 성공")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void patchBrand_Default_Success() throws Exception {
        // given
        PostBrandRequestDTO postBrandRequestDTO = PostBrandRequestDTO.builder()
                .name("브랜드 수정")
                .description("수정된 브랜드 설명")
                .build();

        MockMultipartFile file1 = new MockMultipartFile("brandImages", "test1.jpg", "image/jpeg", "test image content 1".getBytes());

        doNothing().when(brandApplication).postBrandWithBrandImages(postBrandRequestDTO, List.of(file1));

        // when
        mockMvc.perform(multipart("/sellers/brands")
                        .file(file1)
                        .param("form", "{\"name\":\"브랜드 수정\",\"description\":\"수정된 브랜드 설명\"}")
                        .with(csrf())
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is("수정 성공")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void denyBrandPost_Default_Success() throws Exception {
        // given
        Long brandId = 1L;
        doNothing().when(brandApplication).denyBrandPost(brandId); // 정상 동작 설정

        // when, then
        mockMvc.perform(patch("/admins/brand-requests/{brandId}/deny", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is("해당 브랜드 등록이 거부됐습니다.")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void approveBrandPost_Default_Success() throws Exception {
        // given
        Long brandId = 1L;
        doNothing().when(brandApplication).approveBrandPost(brandId); // 정상 동작 설정
        // when, then
        mockMvc.perform(patch("/admins/brand-requests/{brandId}/approve", brandId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.is("해당 브랜드 등록이 승인됐습니다.")))
                .andDo(MockMvcResultHandlers.print());
    }

}
