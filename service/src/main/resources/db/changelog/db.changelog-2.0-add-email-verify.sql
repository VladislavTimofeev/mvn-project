--liquibase formatted sql

--changeset vtsimafeyeu:1
ALTER TABLE users
    ADD COLUMN email_verified BOOLEAN NOT NULL DEFAULT FALSE,
    ADD COLUMN email_verified_at TIMESTAMP;

--changeset vtsimafeyeu:2
CREATE TABLE email_verification_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(100) NOT NULL UNIQUE,
    user_id     BIGINT       NOT NULL,
    expires_at  TIMESTAMP    NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    verified_at TIMESTAMP,
    used        BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_verification_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

--changeset vtsimafeyeu:3
CREATE INDEX idx_verification_token ON email_verification_tokens(token);
CREATE INDEX idx_verification_user_id ON email_verification_tokens(user_id);
CREATE INDEX idx_verification_expires_at ON email_verification_tokens(expires_at);
