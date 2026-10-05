DROP INDEX IF EXISTS idx_event_store_aggregate_version;

CREATE UNIQUE INDEX idx_event_store_aggregate_version
    ON event_store (aggregate_id, version);
