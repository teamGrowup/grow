package org.boot.growup.common.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.boot.growup.common.constant.Status;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(value = {AuditingEntityListener.class})
public abstract class BaseEntity { // 모든 테이블에 대해 자동으로 수정 시간 및 생성 시간이 기록된다.
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime modifiedAt;
    private LocalDateTime deletedAt;
    @Enumerated(EnumType.STRING)
    private Status status = Status.ACTIVE;

    public void delete() {
        this.deletedAt = LocalDateTime.now();
        this.status = Status.INACTIVE;
    }
}
