package org.boot.growup.source.board.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.common.enumerate.InquiryCategory;

@Data
@Builder
public class PostInquiryRequestDTO {
  @NotBlank(message = "문의 제목은 필수 입력입니다.")
  private String title;

  @NotBlank(message = "문의 내용은 필수 입력입니다.")
  private String content;

  @NotNull(message = "문의 카테고리를 선택해주세요.")
  private InquiryCategory category;

}
