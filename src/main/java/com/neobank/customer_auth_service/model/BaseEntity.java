package com.neobank.customer_auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;

import java.util.UUID;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @Column(
            name = "id",
            nullable = false,
            updatable = false,
            length = 36
    )
    private String id;

    @PrePersist
    protected void generateId() {
        if (id == null || id.isBlank()) {
            id = UUID.randomUUID().toString();
        }
    }

    public String getId() {
        return id;
    }
}