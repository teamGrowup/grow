package org.boot.growup.source.board.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.enumerate.InquiryCategory;
import org.boot.growup.source.board.dto.request.PostInquiryRequestDTO;
import org.boot.growup.source.customer.persist.entity.Customer;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@Table(name = "inquiry")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate  // 변경된 필드만 수정
public class Inquiry {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "inquiry_id", nullable = false)
  private Long id;

  @Enumerated(EnumType.STRING)
  @Column(name = "category", nullable = false)
  private InquiryCategory category;

  @Column(nullable = false, length = 50)
  private String title;

  @Column(nullable = false, length = 500)
  private String content;

  private Boolean isAnswered;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  public static Inquiry of(PostInquiryRequestDTO input, Customer customer) {
    return Inquiry.builder()
        .category(input.getInquiryCategory())
        .title(input.getTitle())
        .content(input.getContent())
        .isAnswered(false)
        .customer(customer)
        .build();
  }

  // 답변 등록 완료 처리
  public void completeReply() {
    this.isAnswered = true;
  }
}
