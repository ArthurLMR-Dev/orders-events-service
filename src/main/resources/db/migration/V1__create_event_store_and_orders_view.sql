CREATE TABLE event_store (
    id UUID PRIMARY KEY,
    event_id UUID NOT NULL UNIQUE,
    aggregate_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    payload JSONB NOT NULL,
    version BIGINT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX idx_event_store_aggregate_version
    ON event_store (aggregate_id, version);

CREATE TABLE orders_view (
    id UUID PRIMARY KEY,
    status VARCHAR(20) NOT NULL,
    total NUMERIC(19, 2) NOT NULL,
    version BIGINT NOT NULL,
    persistence_version BIGINT NOT NULL DEFAULT 0,
    last_event_id UUID NOT NULL UNIQUE,
    updated_at TIMESTAMPTZ NOT NULL
);
