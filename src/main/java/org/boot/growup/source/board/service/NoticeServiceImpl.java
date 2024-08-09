package org.boot.growup.source.board.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.boot.growup.source.board.persist.NoticeRepository;
import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NoticeServiceImpl implements NoticeService {

  private final NoticeRepository noticeRepository;

  /**
   * 공지사항 등록
   */
  @Override
  @Transactional
  public Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO) {

    // 1. 관리자 확인 코드 추가 필요!

    //Notice notice = Notice.of(postNoticeRequestDTO, "admin");

    Notice notice = Notice.of(postNoticeRequestDTO, "admin");

    Long id = noticeRepository.save(notice).getId();

    return id;
  }

  @Override
  public List<GetNoticeResponseDTO> getNotice() {

    List<GetNoticeResponseDTO> noticeList = noticeRepository.findAll()
        .stream().map(GetNoticeResponseDTO::from).toList();

    return noticeList;
  }


}

