package com.neobank.customer_auth_service.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseAuditEntity {

    @Column(
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            nullable = false,
            unique = true,
            length = 255
    )
    private String email;

    @Column(
            nullable = false,
            unique = true,
            length = 20
    )
    private String phone;

    @Column(
            nullable = false,
            length = 500
    )
    private String address;

    @Column(
            name = "pan_document",
            nullable = false
    )
    private byte[] panDocument;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "kyc_status",
            nullable = false,
            length = 30
    )
    private KycStatus kycStatus;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 30
    )
    private CustomerStatus status;
}