package org.boot.growup.source.board.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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

  private String admin;

  public static GetNoticeResponseDTO from(Notice notice) {
    return GetNoticeResponseDTO.builder()
        .id(notice.getId())
        .title(notice.getTitle())
        .content(notice.getContent())
        .admin(notice.getAdmin())
        .build();
  }

    /* Page<Entity> -> Page<DTO> 변환 처리 */
  public static Page<GetNoticeResponseDTO> pageFrom(Page<Notice> noticeList) {
    return noticeList.map(m -> from(m));
  }
}



