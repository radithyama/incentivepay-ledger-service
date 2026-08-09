package com.incentivepay.ledger.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Append-only: entries are never updated or deleted, only inserted. The
 * unique constraint on disbursementId is the dedupe guard against Kafka
 * at-least-once redelivery.
 */
@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private UUID disbursementId;

    @Column(nullable = false)
    private String participantExternalRef;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(nullable = false)
    private Instant disbursedAt;

    private String paymentRailConfirmationId;

    @Column(nullable = false, updatable = false)
    private Instant recordedAt = Instant.now();

    protected LedgerEntry() {
        // JPA
    }

    public LedgerEntry(UUID disbursementId, String participantExternalRef, BigDecimal amount, String currency,
                        Instant disbursedAt, String paymentRailConfirmationId) {
        this.disbursementId = disbursementId;
        this.participantExternalRef = participantExternalRef;
        this.amount = amount;
        this.currency = currency;
        this.disbursedAt = disbursedAt;
        this.paymentRailConfirmationId = paymentRailConfirmationId;
    }

    public UUID getId() {
        return id;
    }

    public UUID getDisbursementId() {
        return disbursementId;
    }

    public String getParticipantExternalRef() {
        return participantExternalRef;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public Instant getDisbursedAt() {
        return disbursedAt;
    }

    public String getPaymentRailConfirmationId() {
        return paymentRailConfirmationId;
    }

    public Instant getRecordedAt() {
        return recordedAt;
    }
}
