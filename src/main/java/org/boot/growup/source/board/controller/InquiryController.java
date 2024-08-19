package org.boot.growup.source.board.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.constant.BaseResponse;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.admin.service.AdminService;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.boot.growup.source.board.service.InquiryService;
import org.boot.growup.source.board.service.ReplyService;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.boot.growup.source.customer.service.CustomerService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class InquiryController {
  private final InquiryService inquiryService;
  private final ReplyService replyService;
  private final CustomerService customerService;
  private final AdminService adminService;

  /**
   * [CUSTOMER] 일반 1대1 문의 등록
   * @param input
   * @return
   */
  @PostMapping("/customers/board/inquiry")
  public BaseResponse<Long> postInquiry(@Valid @RequestBody PostInquiryRequestDTO input) {
    Customer customer = customerService.getCurrentCustomer();
    return new BaseResponse<>(inquiryService.postInquiry(input, customer));
  }

  /**
   * [CUSTOMER] 내가 작성한 1대1 문의 정보 조회
   * @param pageNo
   * @return
   */
  @GetMapping("/customers/board/inquiry")
  public BaseResponse<Page<GetInquiryResponseDTO>> getInquiry(@RequestParam(value="pageNo", defaultValue="0") int pageNo) {
    Customer customer = customerService.getCurrentCustomer();
    return new BaseResponse<>(inquiryService.getInquiry(customer.getId(), pageNo));
  }

  /**
   * [ADMIN] 미답변 문의 정보 조회
   * @param pageNo
   * @return
   */
  @GetMapping("/admins/board/inquiry/unanswered")
  public BaseResponse<Page<GetInquiryResponseDTO>> getUnansweredInquiry(
      @RequestParam(value="pageNo", defaultValue="0") int pageNo) {
    return new BaseResponse<>(inquiryService.getUnansweredInquiry(pageNo));
  }

  /**
   * [ADMIN] 문의 답변 등록
   * @param input
   * @param inquiryId
   * @return
   */
  @PostMapping("/admins/board/inquiry/{inquiryId}")
  public BaseResponse<String> postReply(@Valid @RequestBody PostReplyRequestDTO input, @PathVariable Long inquiryId) {
    Admin admin = adminService.getCurrentAdmin();
    Inquiry inquiry = inquiryService.getOneInquiry(inquiryId);
    Long id = replyService.postReply(input, admin, inquiry);
    return new BaseResponse<>("문의 사항 등록 성공. 문의사항 ID : " + id.toString());
  }
}
