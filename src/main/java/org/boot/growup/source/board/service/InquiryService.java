package org.boot.growup.source.board.service;

import lombok.extern.slf4j.Slf4j;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.boot.growup.source.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface InquiryService {
  public Long postInquiry(PostInquiryRequestDTO input, long customer);
  public Page<GetInquiryResponseDTO> getInquiry(long id, int pageNo);
  public Page<GetInquiryResponseDTO> getUnansweredInquiry(int pageNo);
  public Inquiry getOneInquiry(long id);
}