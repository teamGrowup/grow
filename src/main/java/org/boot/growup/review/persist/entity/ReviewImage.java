package org.boot.growup.review.persist.entity;

import lombok.*;

import jakarta.persistence.*;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "review_image")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class ReviewImage extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_image_id", nullable = false)
    private Long id;

    @Column(name = "original_image_name", nullable = false)
    private String originalImageName;

    @Column(name = "path", nullable = false)
    private String path;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private Review review;

    public void designateReview(Review review) {
        this.review = review;
    }
}
