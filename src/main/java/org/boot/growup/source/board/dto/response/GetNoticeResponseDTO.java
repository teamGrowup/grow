package org.boot.growup.source.board.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.data.domain.Page;

@Data
@Builder
public class GetNoticeResponseDTO {

  private Long id;

  private String title;

  private String content;

  private String author;

  public static GetNoticeResponseDTO from(Notice notice) {
    return GetNoticeResponseDTO.builder()
        .id(notice.getId())
        .title(notice.getTitle())
        .content(notice.getContent())
        .author(notice.getAdmin().getEmail())
        .build();
  }

  public static Page<GetNoticeResponseDTO> pageFrom(Page<Notice> noticeList) {
    return noticeList.map(GetNoticeResponseDTO::from);
  }
}



