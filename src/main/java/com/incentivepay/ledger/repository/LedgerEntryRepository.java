package com.incentivepay.ledger.repository;

import com.incentivepay.ledger.domain.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, UUID> {

    boolean existsByDisbursementId(UUID disbursementId);

    List<LedgerEntry> findByParticipantExternalRefOrderByDisbursedAtDesc(String participantExternalRef);
}
