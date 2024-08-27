package org.boot.growup.review.persist.entity;

import lombok.*;
import org.boot.growup.auth.persist.entity.Customer;
import org.boot.growup.common.entity.BaseEntity;
import org.boot.growup.order.persist.entity.OrderItem;

import jakarta.persistence.*;
import org.boot.growup.review.dto.request.PostReviewRequestDTO;
import org.hibernate.envers.AuditOverride;

import java.util.List;

@Entity
@Getter
@Builder
@Table(name = "review")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Review extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id", nullable = false)
    private Long id;

    @Column(name = "author", nullable = false, length = 25)
    private String author;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "rating", nullable = false)
    private Double rating;

    @Column(name = "like_count", nullable = false)
    private int likeCount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewImage> reviewImages;

    public void likeCountPlus() {
        this.likeCount++;
    }

    // 좋아요 수 감소
    public void likeCountMinus() {
        if (this.likeCount > 0) {
            this.likeCount--;
        }
    }

    public void patchReviewInfo(String content, Double rating) {
        this.content = content;
        this.rating = rating;
    }

    public static Review of(PostReviewRequestDTO requestDTO, Customer customer, OrderItem orderItem) {
        return Review.builder()
                .author(requestDTO.getAuthor())
                .content(requestDTO.getContent())
                .rating(requestDTO.getRating())
                .likeCount(0) // 초기 좋아요 수
                .customer(customer)
                .orderItem(orderItem)
                .build();
    }
}
