package com.incentivepay.ledger.web.dto;

import com.incentivepay.ledger.domain.LedgerEntry;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID id,
        UUID disbursementId,
        BigDecimal amount,
        String currency,
        Instant disbursedAt,
        String paymentRailConfirmationId
) {
    public static LedgerEntryResponse from(LedgerEntry entry) {
        return new LedgerEntryResponse(entry.getId(), entry.getDisbursementId(), entry.getAmount(),
                entry.getCurrency(), entry.getDisbursedAt(), entry.getPaymentRailConfirmationId());
    }
}
