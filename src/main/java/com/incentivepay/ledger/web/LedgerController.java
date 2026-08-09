package com.incentivepay.ledger.web;

import com.incentivepay.ledger.domain.LedgerEntry;
import com.incentivepay.ledger.repository.LedgerEntryRepository;
import com.incentivepay.ledger.web.dto.LedgerEntryResponse;
import com.incentivepay.ledger.web.dto.ParticipantLedgerResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/** "How much has this person been paid, and for what" - the reconciliation view (PRD 6.5). */
@RestController
@RequestMapping("/v1/ledger")
public class LedgerController {

    private final LedgerEntryRepository ledgerEntryRepository;

    public LedgerController(LedgerEntryRepository ledgerEntryRepository) {
        this.ledgerEntryRepository = ledgerEntryRepository;
    }

    @GetMapping("/{participantExternalRef}")
    public ParticipantLedgerResponse getForParticipant(@PathVariable String participantExternalRef) {
        List<LedgerEntry> entries = ledgerEntryRepository.findByParticipantExternalRefOrderByDisbursedAtDesc(participantExternalRef);
        BigDecimal total = entries.stream().map(LedgerEntry::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
        List<LedgerEntryResponse> responses = entries.stream().map(LedgerEntryResponse::from).toList();
        return new ParticipantLedgerResponse(participantExternalRef, total, responses);
    }
}
