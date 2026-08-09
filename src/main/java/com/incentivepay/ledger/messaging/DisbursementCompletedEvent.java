package com.incentivepay.ledger.messaging;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Mirrors incentive-api's event of the same name field-for-field. Kept as a
 * separate copy (not a shared library) so each service can evolve its own
 * consumer contract independently - the usual event-driven microservice tradeoff.
 */
public record DisbursementCompletedEvent(
        UUID disbursementId,
        String participantExternalRef,
        BigDecimal amount,
        String currency,
        Instant disbursedAt,
        String paymentRailConfirmationId
) {
}
