package org.boot.growup.source.board.service;

import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.springframework.data.domain.Page;

public interface NoticeService {
  /*
  공지사항 등록
   */
  Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO, Admin admin);

  /*
  공지사항 목록 조회
   */
  Page<GetNoticeResponseDTO> getNotice(int pageNo);

  /*
  공지사항 수정
   */
  Long patchNotice(Long noticeId, PostNoticeRequestDTO postNoticeRequestDTO, Admin admin);

  /*
  공지사항 상세 조회
   */
  GetNoticeResponseDTO getNoticeDetail(long noticeId);

  /*
  공지사항 삭제
   */
  Long deleteNotice(Long noticeId);
}
