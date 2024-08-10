package org.boot.growup.source.board.service;

import java.util.List;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public interface NoticeService {
  public Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO, String admin);

  public Page<GetNoticeResponseDTO> getNotice(int pageNo);

  public Long updateNotice(Long noticeId, PostNoticeRequestDTO postNoticeRequestDTO, String admin);

  public GetNoticeResponseDTO getNoticeDetail(long noticeId);

  public Long deleteNotice(Long noticeId);
}
