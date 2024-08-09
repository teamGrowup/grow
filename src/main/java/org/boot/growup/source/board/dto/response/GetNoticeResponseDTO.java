package org.boot.growup.source.board.dto.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.source.board.persist.entity.Notice;

@Data
@Builder
public class GetNoticeResponseDTO {

  private Long id;

  private String title;

  private String content;

  private LocalDateTime createdAt;

  public static GetNoticeResponseDTO from(Notice notice) {
    return GetNoticeResponseDTO.builder()
        .id(notice.getId())
        .title(notice.getTitle())
        .content(notice.getContent())
        .build();
  }
}



