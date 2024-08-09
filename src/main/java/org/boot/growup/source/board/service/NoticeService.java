package org.boot.growup.source.board.service;

import java.util.List;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.boot.growup.source.board.dto.response.GetNoticeResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface NoticeService {

  public Long postNotice(PostNoticeRequestDTO postNoticeRequestDTO);

  public List<GetNoticeResponseDTO> getNotice();

}
