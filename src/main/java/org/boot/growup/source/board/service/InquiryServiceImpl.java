package org.boot.growup.source.board.service;

import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.source.board.persist.InquiryRepository;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InquiryServiceImpl implements InquiryService {

  private final InquiryRepository inquiryRepository;

  @Override
  @Transactional
  public Long postInquiry(PostInquiryRequestDTO input, long customer) {
    // DTO -> Entity
    Inquiry inquiry = Inquiry.of(input, customer);

    return inquiryRepository.save(inquiry).getId();
  }

  @Override
  public Page<GetInquiryResponseDTO> getInquiry(long id, int pageNo) {

    List<Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("id")); // 정렬기준
    Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sorts)); // Pageable 설정

    // 데이터 조회 (수정필요)
    Page<Inquiry> inquiry = inquiryRepository.findByCustomer(id, pageable);

    return GetInquiryResponseDTO.pageFrom(inquiry);
  }
}
