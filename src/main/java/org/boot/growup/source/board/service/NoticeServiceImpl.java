package org.boot.growup.source.board.service;

import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.boot.growup.common.error.BaseException;
import org.boot.growup.common.error.ErrorCode;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.source.board.persist.repository.NoticeRepository;
import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {
  private final NoticeRepository noticeRepository;

  @Transactional
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

  @Transactional
  @Override
  public Long updateNotice(Long noticeId, PostNoticeRequestDTO postNoticeRequestDTO, Admin admin) {
    Notice beforeNotice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));
    beforeNotice.changeData(postNoticeRequestDTO, admin);

    return beforeNotice.getId();
  }

  @Override
  public GetNoticeResponseDTO getNoticeDetail(long noticeId) {
    Notice notice = noticeRepository.findById(noticeId)
        .orElseThrow(() -> new BaseException(ErrorCode.NOTICE_NOT_FOUND));

    return GetNoticeResponseDTO.from(notice);
  }

  @Transactional
  @Override
  public Long deleteNotice(Long noticeId) {
    if (!noticeRepository.existsById(noticeId)) {
      throw new BaseException(ErrorCode.NOTICE_NOT_FOUND);
    }

    noticeRepository.deleteById(noticeId);

    return noticeId;
  }


}

