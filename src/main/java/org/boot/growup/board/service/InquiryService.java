package org.boot.growup.board.service;

import org.boot.growup.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.board.persist.entity.Inquiry;
import org.boot.growup.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.data.domain.Page;

public interface InquiryService {
  /*
  일대일 문의(Inquiry) 등록
   */
  Long postInquiry(PostInquiryRequestDTO input, Customer customer);

  /*
  일대일 문의(Inquiry) 목록 조회
   */
  Page<GetInquiryResponseDTO> getInquiry(Long id, int pageNo);

  /*
  일대일 문의(Inquiry) 미답변 조회
   */
  Page<GetInquiryResponseDTO> getUnansweredInquiry(int pageNo);

  /*
  일대일 문의(Inquiry) 상세 조회
   */
  Inquiry getOneInquiry(long id);
}
