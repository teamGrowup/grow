package org.boot.growup.source.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.source.board.persist.entity.InquiryCategory;

@Data
@Builder
public class PostInquiryRequestDTO {

  private InquiryCategory inquiryCategory;

  @NotBlank(message = "문의 제목은 필수 입력입니다.")
  private String title;

  @NotBlank(message = "문의 내용은 필수 입력입니다.")
  private String content;

}
