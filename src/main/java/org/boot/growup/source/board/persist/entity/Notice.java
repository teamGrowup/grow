package org.boot.growup.source.board.persist.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.source.board.dto.request.PostNoticeRequestDTO;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.domain.Page;

@Entity
@Getter
@Builder
@Table(name = "notice")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate  // 변경된 필드만 수정
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "notice_id", nullable = false)
  private Long id;

  private String title;
  private String content;

//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "admin_id")
//  private Admin admin;
  private String admin;

  public static Notice of(PostNoticeRequestDTO postNoticeRequestDTO, String admin) {
    return Notice.builder()
        .title(postNoticeRequestDTO.getTitle())
        .content(postNoticeRequestDTO.getContent())
        .admin("admin")
        .build();
  }

  /* 공지사항 데이터 수정 */
  public void changeData(PostNoticeRequestDTO postNoticeRequestDTO, String admin) {
    this.title = postNoticeRequestDTO.getTitle();
    this.content = postNoticeRequestDTO.getContent();
    this.admin = admin;
  }

}






