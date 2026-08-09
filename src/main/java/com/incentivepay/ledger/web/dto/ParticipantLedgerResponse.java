package com.incentivepay.ledger.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record ParticipantLedgerResponse(
        String participantExternalRef,
        BigDecimal totalPaid,
        List<LedgerEntryResponse> entries
) {
}
