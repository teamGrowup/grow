package org.boot.growup.source.board.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.source.admin.persist.entity.Admin;
import org.boot.growup.source.board.dto.request.PostReplyRequestDTO;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Getter
@Builder
@Table(name = "reply")
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate  // 변경된 필드만 수정
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reply_id", nullable = false)
    private Long id;

    private String title;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin admin;

    public static Reply of(PostReplyRequestDTO input, Admin admin) {
        return Reply.builder()
                .title(input.getTitle())
                .content(input.getContent())
                .admin(admin)
                .build();
    }

}
