package org.boot.growup.board.dto.response;

import lombok.Builder;
import lombok.Data;
import org.boot.growup.board.persist.entity.Inquiry;
import org.boot.growup.common.constant.InquiryCategory;
import org.boot.growup.board.persist.entity.Reply;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;

@Data
@Builder
public class GetInquiryResponseDTO {
  private Long id;
  private InquiryCategory category;
  private String title;
  private String content;
  private Boolean isAnswered;
  private String author;
  private ReplyDTO reply;

  @Data
  @Builder
  public static class ReplyDTO {
    private String title;
    private String content;
    private String author;

    public static ReplyDTO from(Reply reply) {
      return ReplyDTO.builder()
              .title(reply.getTitle())
              .content(reply.getContent())
              .author(reply.getAdmin().getEmail())
              .build();
    }
  }

  public static GetInquiryResponseDTO from(Inquiry inquiry) {
    if (ObjectUtils.isEmpty(inquiry.getReply())) {
      return GetInquiryResponseDTO.builder()
              .id(inquiry.getId())
              .category(inquiry.getCategory())
              .title(inquiry.getTitle())
              .content(inquiry.getContent())
              .isAnswered(inquiry.getIsAnswered())
              .author(inquiry.getCustomer().getName())
              .build();
    }

    return GetInquiryResponseDTO.builder()
        .id(inquiry.getId())
        .category(inquiry.getCategory())
        .title(inquiry.getTitle())
        .content(inquiry.getContent())
        .isAnswered(inquiry.getIsAnswered())
        .author(inquiry.getCustomer().getName())
        .reply(ReplyDTO.from(inquiry.getReply()))
        .build();
  }

  public static Page<GetInquiryResponseDTO> pageFrom(Page<Inquiry> inquiryList) {
    return inquiryList.map(GetInquiryResponseDTO::from);
  }

}
