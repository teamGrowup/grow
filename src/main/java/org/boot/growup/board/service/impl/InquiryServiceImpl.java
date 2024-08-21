package org.boot.growup.board.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.board.dto.response.GetInquiryResponseDTO;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.board.persist.entity.Inquiry;
import org.boot.growup.board.persist.repository.InquiryRepository;
import org.boot.growup.board.service.InquiryService;
import org.boot.growup.auth.persist.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class InquiryServiceImpl implements InquiryService {
  private final InquiryRepository inquiryRepository;

  @Override
  public Long postInquiry(PostInquiryRequestDTO input, Customer customer) {
    Inquiry inquiry = Inquiry.of(input, customer);
    return inquiryRepository.save(inquiry).getId();
  }

  @Override
  public Page<GetInquiryResponseDTO> getInquiry(Long id, int pageNo) {

    List<Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("id"));
    Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sorts));
    Page<Inquiry> inquiryList = inquiryRepository.findByCustomer_Id(id, pageable);

    return GetInquiryResponseDTO.pageFrom(inquiryList);
  }

  @Override
  public Page<GetInquiryResponseDTO> getUnansweredInquiry(int pageNo) {

    List<Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.asc("id"));
    Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sorts));

    Page<Inquiry> inquiryList = inquiryRepository.findByIsAnswered(false, pageable);

    return GetInquiryResponseDTO.pageFrom(inquiryList);
  }

  @Override
  public Inquiry getOneInquiry(long id) {
    return inquiryRepository.findById(id)
            .orElseThrow(() -> new BaseException(ErrorCode.INQUIRY_NOT_FOUND));
  }
}
