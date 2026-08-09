CREATE TABLE ledger_entries (
    id                             UUID PRIMARY KEY,
    disbursement_id                UUID          NOT NULL UNIQUE,
    participant_external_ref       VARCHAR(255)  NOT NULL,
    amount                         NUMERIC(19,2) NOT NULL,
    currency                       VARCHAR(3)    NOT NULL,
    disbursed_at                   TIMESTAMPTZ   NOT NULL,
    payment_rail_confirmation_id   VARCHAR(255),
    recorded_at                    TIMESTAMPTZ   NOT NULL
);

CREATE INDEX idx_ledger_entries_participant ON ledger_entries(participant_external_ref);
