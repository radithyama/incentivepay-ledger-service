package com.incentivepay.ledger.service;

import com.incentivepay.ledger.domain.LedgerEntry;
import com.incentivepay.ledger.messaging.DisbursementCompletedEvent;
import com.incentivepay.ledger.repository.LedgerEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class LedgerRecordingService {

    private static final Logger log = LoggerFactory.getLogger(LedgerRecordingService.class);

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerRecordingService(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @Transactional
    public void record(DisbursementCompletedEvent event) {
        if (ledgerEntryRepository.existsByDisbursementId(event.disbursementId())) {
            log.info("Skipping duplicate disbursement.completed delivery for disbursement {}", event.disbursementId());
            return;
        }
        LedgerEntry entry = new LedgerEntry(
                event.disbursementId(),
                event.participantExternalRef(),
                event.amount(),
                event.currency(),
                event.disbursedAt(),
                event.paymentRailConfirmationId()
        );
        ledgerEntryRepository.save(entry);
    }
}
