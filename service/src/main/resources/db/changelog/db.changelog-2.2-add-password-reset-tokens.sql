--liquibase formatted sql

--changeset vtsimafeyeu:1
CREATE TABLE password_reset_tokens
(
    id         BIGSERIAL PRIMARY KEY,
    token      VARCHAR(100) NOT NULL UNIQUE,
    user_id    BIGINT       NOT NULL,
    expires_at TIMESTAMP    NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    used_at    TIMESTAMP,
    used       BOOLEAN      NOT NULL DEFAULT FALSE,
    CONSTRAINT fk_password_reset_token_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

--changeset vtsimafeyeu:2
CREATE INDEX idx_password_reset_token ON password_reset_tokens (token);
CREATE INDEX idx_password_reset_user_id ON password_reset_tokens (user_id);
CREATE INDEX idx_password_reset_expires_at ON password_reset_tokens (expires_at);
