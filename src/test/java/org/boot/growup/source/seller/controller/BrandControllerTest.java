package org.boot.growup.source.seller.controller;

import org.boot.growup.auth.utils.JwtAuthenticationFilter;
import org.boot.growup.auth.utils.JwtTokenProvider;
import org.boot.growup.product.application.BrandApplication;
import org.boot.growup.product.controller.BrandController;
import org.boot.growup.product.dto.response.GetSellerBrandResponseDTO;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ActiveProfiles("test")
@WebMvcTest(controllers = BrandController.class)
@WithMockUser
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
    public void getSellerBrand_Default_Success() throws Exception{
        //given
        given(brandApplication.getSellerBrand()).willReturn(
                GetSellerBrandResponseDTO.builder()
                        .name("브랜드1")
                        .description("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다.")
                        .brandImages(
                                List.of(GetSellerBrandResponseDTO.BrandImageDTO.builder()
                                        .path("aws/s3/path1/brand1")
                                        .originalImageName("brand1.jpg")
                                        .build()
                                )
                        )
                        .build()
        );

        //when, then
        mockMvc.perform(get("/sellers/brands"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code", Matchers.is(200)))
                .andExpect(jsonPath("$.message", Matchers.is("요청에 성공하였습니다.")))
                .andExpect(jsonPath("$.isSuccess", Matchers.is(true)))
                .andExpect(jsonPath("$.data.name", Matchers.is("브랜드1")))
                .andExpect(jsonPath("$.data.description", Matchers.is("브랜드1은 심플한 디자인과 고급스러운 소재를 활용한 제품을 선보입니다.")))
                .andExpect(jsonPath("$.data.brandImages[0].path", Matchers.is("aws/s3/path1/brand1")))
                .andExpect(jsonPath("$.data.brandImages[0].originalImageName", Matchers.is("brand1.jpg")))
                .andDo(MockMvcResultHandlers.print(System.out));

    }



}