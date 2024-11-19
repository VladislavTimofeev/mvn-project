--liquibase formatted sql

--changeset vtsimafeyeu:1
ALTER TABLE request
ADD COLUMN created_at TIMESTAMP;

ALTER TABLE request
ADD COLUMN modified_at TIMESTAMP;

ALTER TABLE request
ADD COLUMN created_by VARCHAR(32);

ALTER TABLE request
ADD COLUMN modified_by VARCHAR(32);

--changeset vtsimafeyeu:2
CREATE TABLE IF NOT EXISTS revision (
    id SERIAL PRIMARY KEY,
    timestamp BIGINT
);
--rollback DROP TABLE revision;

--changeset vtsimafeyeu:3
CREATE TABLE IF NOT EXISTS request_aud
(
    id               BIGINT,
    rev              INT            REFERENCES revision (id),
    revtype          SMALLINT,
    created_at       TIMESTAMP,
    created_by       VARCHAR(32),
    modified_at      TIMESTAMP,
    modified_by      VARCHAR(32),
    customer_id      BIGINT,
    status           VARCHAR(255),
    cargo_details    TEXT,
    weight           DECIMAL(10, 2),
    pallet_count     INT,
    refrigerated     BOOLEAN,
    pickup_address   VARCHAR(255),
    delivery_address VARCHAR(255),
    creation_date    TIMESTAMP,
    carrier_id       BIGINT
    );
--rollback DROP TABLE request_aud
