package org.boot.growup.source.board.controller;

import com.fasterxml.jackson.databind.ser.Serializers.Base;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.source.board.service.InquiryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/board/inquiry")
@RequiredArgsConstructor
public class InquiryController {

  private final InquiryService inquiryService;

  /**
   * 1. 문의 등록
   */
  @PostMapping
  public void postInquiry(@Valid @RequestBody PostInquiryRequestDTO input) {
    // 로그인 정보 가져오기
    long customer = 1L;

    // 문의 등록
    Long id = inquiryService.postInquiry(input, customer);
  }

  /**
   * 2. [사용자] 문의 조회
   */
  @GetMapping
  public BaseResponse<Page<GetInquiryResponseDTO>> getInquiry(
      @RequestParam(value="pageNo", defaultValue="0") int pageNo) {
    // 로그인 확인
    long customer = 1L;

    // 내가 작성한 문의 조회
    Page<GetInquiryResponseDTO> result = inquiryService.getInquiry(customer, pageNo);

    return new BaseResponse<>(result);
  }

  /**
   * 3. [관리자] 미답변 문의 조회
   */
  @GetMapping("/unanswered")
  public BaseResponse<Page<GetInquiryResponseDTO>> getUnansweredInquiry(
      @RequestParam(value="pageNo", defaultValue="0") int pageNo) {
    // 관리자 확인
    long admin = 1L;

    // 미답변 문의 조회
    Page<GetInquiryResponseDTO> result = inquiryService.getUnansweredInquiry(pageNo);

    return new BaseResponse<>(result);
  }

}
