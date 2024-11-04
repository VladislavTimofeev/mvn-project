--liquibase formatted sql

--changeset vtsimafeyeu:1
CREATE TABLE IF NOT EXISTS users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(255) UNIQUE NOT NULL,
    password     VARCHAR(255) NOT NULL,
    role         VARCHAR(255) NOT NULL DEFAULT 'GUEST',
    name         VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255),
    address      VARCHAR(255)
);
-- rollback DROP TABLE users;

--changeset vtsimafeyeu:2
CREATE TABLE IF NOT EXISTS request
(
    id               SERIAL PRIMARY KEY,
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
-- rollback DROP TABLE request;

--changeset vtsimafeyeu:3
CREATE TABLE IF NOT EXISTS vehicle
(
    id              SERIAL PRIMARY KEY,
    carrier_id      BIGINT         NOT NULL,
    license_plate   VARCHAR(50)    NOT NULL,
    capacity        DECIMAL(10, 2) NOT NULL,
    pallet_capacity INT            NOT NULL,
    refrigerated    BOOLEAN        NOT NULL,
    model           VARCHAR(255),
    FOREIGN KEY (carrier_id) REFERENCES users (id)
);
-- rollback DROP TABLE vehicle;

--changeset vtsimafeyeu:4
CREATE TABLE IF NOT EXISTS driver
(
    id             SERIAL PRIMARY KEY,
    carrier_id     BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    license_number VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(20),
    FOREIGN KEY (carrier_id) REFERENCES users (id)
);
-- rollback DROP TABLE driver;

--changeset vtsimafeyeu:5
CREATE TABLE IF NOT EXISTS trip
(
    id             SERIAL PRIMARY KEY,
    request_id     BIGINT      NOT NULL,
    vehicle_id     BIGINT      NOT NULL,
    driver_id      BIGINT      NOT NULL,
    departure_time TIMESTAMP,
    arrival_time   TIMESTAMP,
    status         VARCHAR(255) NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (request_id) REFERENCES request (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicle (id),
    FOREIGN KEY (driver_id) REFERENCES driver (id)
);
-- rollback DROP TABLE trip;