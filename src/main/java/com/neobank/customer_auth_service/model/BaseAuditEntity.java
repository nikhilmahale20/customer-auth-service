package com.neobank.customer_auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@MappedSuperclass
@EntityListeners(
        AuditingEntityListener.class
)
public abstract class BaseAuditEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false
    )
    private String id;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(
            name = "updated_at",
            nullable = false
    )
    private OffsetDateTime updatedAt;

    @CreatedBy
    @Column(
            name = "created_by",
            nullable = false,
            updatable = false,
            length = 100
    )
    private String createdBy;

    @LastModifiedBy
    @Column(
            name = "updated_by",
            nullable = false,
            length = 100
    )
    private String updatedBy;

    @PrePersist
    protected void onCreate() {

        if (this.id == null) {

            this.id =
                    UUID.randomUUID()
                            .toString();
        }

        OffsetDateTime now =
                OffsetDateTime.now();

        this.createdAt = now;
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {

        this.updatedAt =
                OffsetDateTime.now();
    }
}