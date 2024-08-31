package org.boot.growup.product.persist.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.boot.growup.common.entity.BaseEntity;
import org.hibernate.envers.AuditOverride;

@Entity
@Getter
@Builder
@Table(name = "search")
@NoArgsConstructor
@AllArgsConstructor
@AuditOverride(forClass = BaseEntity.class)
public class Search extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "search_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String keyword;

    @Column(nullable = false)
    private int searchedCount;

    public void incrementSearchCount() {
        this.searchedCount += 1;
    }
}
