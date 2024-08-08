package org.boot.growup.source.board.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PostNoticeRequestDTO {

  @NotBlank(message = "제목은 필수 입력입니다.")
  private String title;

  @NotBlank(message = "내용은 필수 입력입니다.")
  private String content;

  @JsonCreator
  public PostNoticeRequestDTO(
      @JsonProperty("title") String title,
      @JsonProperty("content") String content) {
    this.title = title;
    this.content = content;
  }

  public static PostNoticeRequestDTO from(PostNoticeRequestDTO notice) {
    return PostNoticeRequestDTO.builder()
        .title(notice.getTitle())
        .content(notice.getContent())
        .build();
  }
}


