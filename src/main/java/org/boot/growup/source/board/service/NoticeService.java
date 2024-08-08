package org.boot.growup.source.board.service;

import lombok.RequiredArgsConstructor;
import org.boot.growup.source.board.dto.NoticePostDTO;
import org.boot.growup.source.board.persist.NoticeRepository;
import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeService {

  private final NoticeRepository noticeRepository;

  /**
   * 공지사항 작성
   */
  @Transactional
  public Long postNotice(NoticePostDTO noticePostDTO) {
    // 관리자 확인 코드 추가 필요!

    Notice notice = Notice.from(noticePostDTO, "admin");
    Long id = noticeRepository.save(notice).getNotice_id();

    return id;
  }
}
