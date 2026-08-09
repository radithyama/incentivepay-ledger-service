package com.incentivepay.ledger.messaging;

import com.incentivepay.ledger.service.LedgerRecordingService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class DisbursementCompletedListener {

    private final LedgerRecordingService ledgerRecordingService;

    public DisbursementCompletedListener(LedgerRecordingService ledgerRecordingService) {
        this.ledgerRecordingService = ledgerRecordingService;
    }

    @KafkaListener(topics = "disbursement.completed", groupId = "ledger-service")
    public void onDisbursementCompleted(DisbursementCompletedEvent event) {
        ledgerRecordingService.record(event);
    }
}
