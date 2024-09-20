CREATE TABLE wallet (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE transaction (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    wallet_id BIGINT NOT NULL,
    amount DECIMAL(19, 2) NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP NOT NULL,
    FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);

CREATE INDEX idx_wallet_id ON transaction(wallet_id);
CREATE INDEX idx_created_at ON transaction(created_at);