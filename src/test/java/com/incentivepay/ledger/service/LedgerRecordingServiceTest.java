package com.incentivepay.ledger.service;

import com.incentivepay.ledger.domain.LedgerEntry;
import com.incentivepay.ledger.messaging.DisbursementCompletedEvent;
import com.incentivepay.ledger.repository.LedgerEntryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LedgerRecordingServiceTest {

    @Mock
    private LedgerEntryRepository ledgerEntryRepository;

    private LedgerRecordingService service;

    private DisbursementCompletedEvent sampleEvent(UUID disbursementId) {
        return new DisbursementCompletedEvent(disbursementId, "EMP-1", new BigDecimal("100.00"),
                "USD", Instant.now(), "SIM-abc");
    }

    @Test
    void recordsNewEventAsALedgerEntry() {
        service = new LedgerRecordingService(ledgerEntryRepository);
        UUID disbursementId = UUID.randomUUID();
        when(ledgerEntryRepository.existsByDisbursementId(disbursementId)).thenReturn(false);

        service.record(sampleEvent(disbursementId));

        ArgumentCaptor<LedgerEntry> captor = ArgumentCaptor.forClass(LedgerEntry.class);
        verify(ledgerEntryRepository).save(captor.capture());
        assertThat(captor.getValue().getDisbursementId()).isEqualTo(disbursementId);
        assertThat(captor.getValue().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void skipsDuplicateDeliveryOfTheSameDisbursement() {
        service = new LedgerRecordingService(ledgerEntryRepository);
        UUID disbursementId = UUID.randomUUID();
        when(ledgerEntryRepository.existsByDisbursementId(disbursementId)).thenReturn(true);

        service.record(sampleEvent(disbursementId));

        verify(ledgerEntryRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }
}
