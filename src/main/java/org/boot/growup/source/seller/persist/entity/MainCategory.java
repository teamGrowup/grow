package org.boot.growup.source.seller.persist.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import jakarta.persistence.*;

@Entity
@Getter
@Builder
@Table(name = "main_category")
@NoArgsConstructor
@AllArgsConstructor
public class MainCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "main_category_id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false, length = 50) // 추가된 어노테이션
    private String name; // 메인 카테고리 이름
}

