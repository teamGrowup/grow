package org.boot.growup.board.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.board.persist.entity.Notice;
import org.boot.growup.board.service.NoticeService;
import org.boot.growup.common.model.BaseException;
import org.boot.growup.common.constant.ErrorCode;
import org.boot.growup.auth.persist.entity.Admin;
import org.boot.growup.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.board.persist.repository.NoticeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeServiceImpl implements NoticeService {
  private final NoticeRepository noticeRepository;

  @Override
  public Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO, Admin admin) {
    Notice notice = Notice.of(postNoticeRequestDTO, admin);
    return noticeRepository.save(notice).getId();
  }

  @Override
  public Page<GetNoticeResponseDTO> getNotice(int pageNo) {
    List<Sort.Order> sorts = new ArrayList<>();
    sorts.add(Sort.Order.desc("id")); // 정렬기준 (엔티티명 기준)
    Pageable pageable = PageRequest.of(pageNo, 10, Sort.by(sorts)); // Pageable 설정

    Page<Notice> noticeList = noticeRepository.findAll(pageable);

    return GetNoticeResponseDTO.pageFrom(noticeList);
  }

  @Override
  public Long patchNotice(Long noticeId, PostNoticeRequestDTO postNoticeRequestDTO, Admin admin) {
    Notice beforeNotice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
    beforeNotice.changeData(postNoticeRequestDTO, admin);
    noticeRepository.save(beforeNotice);

    return beforeNotice.getId();
  }

  @Override
  public GetNoticeResponseDTO getNoticeDetail(long noticeId) {
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new BaseException(ErrorCode.NOTICE_NOT_FOUND));

    return GetNoticeResponseDTO.from(notice);
  }

  @Override
  public Long deleteNotice(Long noticeId) {
    if (!noticeRepository.existsById(noticeId)) {
      throw new BaseException(ErrorCode.NOTICE_NOT_FOUND);
    }

    noticeRepository.deleteById(noticeId);

    return noticeId;
  }


}

