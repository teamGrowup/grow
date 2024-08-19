package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@Table(name = "sub_category")
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_category_id", nullable = false) // 추가된 어노테이션
    private Long id;

    @Column(name = "name", nullable = false, length = 50) // 추가된 어노테이션
    private String name;

    @ManyToOne
    @JoinColumn(name = "main_category_id", nullable = false)
    private MainCategory mainCategory; // 메인 카테고리와의 관계
    // 메인 카테고리의 getter 메서드

    public MainCategory getMainCategory() { return mainCategory; }

}
