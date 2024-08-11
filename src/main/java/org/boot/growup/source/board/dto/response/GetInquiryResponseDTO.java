package org.boot.growup.source.board.dto.response;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Data;
import org.boot.growup.source.board.persist.entity.Inquiry;
import org.boot.growup.source.board.persist.entity.InquiryCategory;
import org.boot.growup.source.board.persist.entity.Notice;
import org.springframework.data.domain.Page;

@Data
@Builder
public class GetInquiryResponseDTO {

  private Long id;
  private InquiryCategory category;
  private String title;
  private String content;
  private Boolean isAnswered;
//  private LocalDateTime createdAt;
  private String createdAt;
  private long customer;

  public static GetInquiryResponseDTO from(Inquiry inquiry) {
    return GetInquiryResponseDTO.builder()
        .id(inquiry.getId())
        .category(inquiry.getCategory())
        .title(inquiry.getTitle())
        .content(inquiry.getContent())
        .isAnswered(inquiry.isAnswered())
        .createdAt("수정 필요")
        .customer(inquiry.getCustomer())
        .build();
  }

  /* Page<Entity> -> Page<DTO> 변환 처리 */
  public static Page<GetInquiryResponseDTO> pageFrom(Page<Inquiry> inquiryList) {
    return inquiryList.map(m -> from(m));
  }

}
