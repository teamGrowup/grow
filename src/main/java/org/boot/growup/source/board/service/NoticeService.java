package org.boot.growup.source.board.service;

import java.util.List;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface NoticeService {
  Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO, String admin);
  Page<GetNoticeResponseDTO> getNotice(int pageNo);
  Long updateNotice(Long noticeId, PostNoticeRequestDTO postNoticeRequestDTO, String admin);
  GetNoticeResponseDTO getNoticeDetail(long noticeId);
  Long deleteNotice(Long noticeId);
}
