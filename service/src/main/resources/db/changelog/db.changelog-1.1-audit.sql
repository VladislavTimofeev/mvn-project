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
    timestamp BIGINT NOT NULL
);
--rollback DROP TABLE revision;

--changeset vtsimafeyeu:3
CREATE TABLE IF NOT EXISTS request_aud
(
    id               BIGINT,
    rev              INT            REFERENCES revision (id),
    revtype                         SMALLINT,
    created_at                      TIMESTAMP,
    created_by                      VARCHAR(32),
    modified_at                     TIMESTAMP,
    modified_by                     VARCHAR(32),
    customer_id      BIGINT         NOT NULL,
    status           VARCHAR(255)   NOT NULL DEFAULT 'PENDING',
    cargo_details    TEXT           NOT NULL,
    weight           DECIMAL(10, 2) NOT NULL,
    pallet_count     INT            NOT NULL,
    refrigerated     BOOLEAN        NOT NULL,
    pickup_address   VARCHAR(255)   NOT NULL,
    delivery_address VARCHAR(255)   NOT NULL,
    creation_date    TIMESTAMP      DEFAULT CURRENT_TIMESTAMP,
    carrier_id       BIGINT,
    FOREIGN KEY (customer_id) REFERENCES users (id),
    FOREIGN KEY (carrier_id) REFERENCES users (id)
    );
--rollback DROP TABLE request_aud
