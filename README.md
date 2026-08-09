# incentivepay-ledger-service

[![CI](https://github.com/radithyama/incentivepay-ledger-service/actions/workflows/ci.yml/badge.svg)](https://github.com/radithyama/incentivepay-ledger-service/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

Append-only reconciliation ledger for [IncentivePay](https://github.com/radithyama/incentivepay-platform).
Part of a 5-repo system; see the [platform repo](https://github.com/radithyama/incentivepay-platform) for the
full architecture, the PRD, and how to run everything together.

## What this service does

Consumes `disbursement.completed` events published by
[`incentivepay-incentive-api`](https://github.com/radithyama/incentivepay-incentive-api), writes one
append-only ledger row per disbursement (never updated or deleted), and exposes the reconciliation view:
"how much has this participant been paid, and for what."

Dedupe is on `disbursementId` (unique DB constraint + an existence check before insert) - Kafka's
at-least-once delivery means the same event can arrive twice; this service makes sure that never becomes a
double-counted ledger entry.

## Quickstart (this service standalone)

Requires Docker.

```bash
docker-compose up --build
```

Brings up its own Postgres, Redpanda, and Keycloak, then the service on **http://localhost:8082**. Note:
nothing will actually land in the ledger from this compose file alone, since nothing is publishing to
`disbursement.completed` - pair it with `incentivepay-incentive-api` pointed at the same Kafka (override its
`KAFKA_BOOTSTRAP_SERVERS`), or use
[`incentivepay-platform`](https://github.com/radithyama/incentivepay-platform) for the full stack wired
together.

### Running without Docker

```bash
export DB_URL=jdbc:postgresql://localhost:5432/ledger_service
export DB_USER=ledger_service
export DB_PASSWORD=ledger_service
export KAFKA_BOOTSTRAP_SERVERS=localhost:9092
export KEYCLOAK_JWK_SET_URI=http://localhost:8081/realms/incentivepay/protocol/openid-connect/certs

mvn spring-boot:run
```

## Running tests

```bash
mvn test
```

No Docker needed - `LedgerRecordingServiceTest` covers the dedupe logic (new event → recorded; duplicate
`disbursementId` → skipped, not re-inserted) with Mockito, no real Kafka/Postgres required.

## API surface

```
GET /v1/ledger/{participantExternalRef}
```

Returns every ledger entry for that participant plus a running total. Open to any authenticated role
(`finance-ops` and `viewer` are the primary consumers per the PRD).

## Security

Keycloak OAuth2 only - every `/v1/**` request needs a valid bearer token, role-agnostic (this service has no
mutating HTTP endpoints, so there's nothing to HMAC-sign; the only way data gets into this service is the
internal Kafka consumer). Validated against `jwk-set-uri`, not `issuer-uri` - see the platform README's "Why
`jwk-set-uri`" section for why that matters in Docker.

## Repo layout

```
src/main/java/com/incentivepay/ledger/
  domain/      LedgerEntry entity
  repository/  LedgerEntryRepository
  service/     LedgerRecordingService (dedupe + persist)
  messaging/   Kafka consumer + local copy of the DisbursementCompletedEvent contract
  security/    Keycloak JWT role mapping, SecurityConfig
  web/         LedgerController
src/main/resources/db/migration/   Flyway migrations (source of truth for schema)
keycloak/realm-export.json         Standalone-compose copy of the realm (see platform repo for the canonical one)
```

## License

MIT - see [LICENSE](LICENSE).
