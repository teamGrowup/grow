package org.boot.growup.source.board.service;

import lombok.extern.slf4j.Slf4j;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface InquiryService {
  Long postInquiry(PostInquiryRequestDTO input, Customer customer);
  Page<GetInquiryResponseDTO> getInquiry(Long id, int pageNo);
  Page<GetInquiryResponseDTO> getUnansweredInquiry(int pageNo);
  Inquiry getOneInquiry(long id);
}
