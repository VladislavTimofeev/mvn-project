-- Создаем енам для ролей
CREATE TYPE role_enum AS ENUM ('CUSTOMER', 'CARRIER', 'ADMIN', 'GUEST');

-- Таблица для хранения данных о пользователях системы для логина
CREATE TABLE users
(
    id           SERIAL PRIMARY KEY,
    username     VARCHAR(255) NOT NULL,
    password     VARCHAR(255) NOT NULL,
    role         role_enum    NOT NULL DEFAULT 'GUEST',
    name         VARCHAR(255) NOT NULL,
    contact_info VARCHAR(255),
    address      VARCHAR(255)
);

-- Создаем енам для статуса заявки
CREATE TYPE request_status_enum AS ENUM ('PENDING', 'IN_PROGRESS', 'REJECTED', 'COMPLETED');

-- Таблица заявок
CREATE TABLE requests
(
    id               SERIAL PRIMARY KEY,
    customer_id      BIGINT         NOT NULL,
    status           request_status_enum NOT NULL DEFAULT 'PENDING',
    cargo_details    TEXT           NOT NULL,
    weight           DECIMAL(10, 2) NOT NULL,
    pallet_count     INT            NOT NULL,
    refrigerated     BOOLEAN        NOT NULL,
    pickup_address   VARCHAR(255)   NOT NULL,
    delivery_address VARCHAR(255)   NOT NULL,
    creation_date    TIMESTAMP               DEFAULT CURRENT_TIMESTAMP,
    carrier_id       BIGINT,
    FOREIGN KEY (customer_id) REFERENCES users (id),
    FOREIGN KEY (carrier_id) REFERENCES users (id)
);

-- Таблица транспорта
CREATE TABLE vehicles
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

-- Таблица водителей
CREATE TABLE drivers
(
    id             SERIAL PRIMARY KEY,
    carrier_id     BIGINT       NOT NULL,
    name           VARCHAR(255) NOT NULL,
    license_number VARCHAR(50)  NOT NULL,
    phone_number   VARCHAR(20),
    FOREIGN KEY (carrier_id) REFERENCES users (id)
);

-- Создаем енам для рейсов
CREATE TYPE trip_status_enum AS ENUM ('PENDING', 'IN_PROGRESS', 'COMPLETED');

-- Таблица рейсов
CREATE TABLE trips
(
    id             SERIAL PRIMARY KEY,
    request_id     BIGINT      NOT NULL,
    vehicle_id     BIGINT      NOT NULL,
    driver_id      BIGINT      NOT NULL,
    departure_time TIMESTAMP,
    arrival_time   TIMESTAMP,
    status         trip_status_enum NOT NULL DEFAULT 'PENDING',
    FOREIGN KEY (request_id) REFERENCES requests (id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles (id),
    FOREIGN KEY (driver_id) REFERENCES drivers (id)
);