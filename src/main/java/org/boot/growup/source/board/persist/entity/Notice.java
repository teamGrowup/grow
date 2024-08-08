package org.boot.growup.source.board.persist.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.source.board.dto.PostNoticeRequestDTO;

@Entity
@Getter
@Builder
@Table(name = "notice")
@NoArgsConstructor
@AllArgsConstructor
public class Notice {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long noticeId;

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
}






