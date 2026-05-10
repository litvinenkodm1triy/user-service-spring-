CREATE SEQUENCE IF NOT EXISTS users_seq START WITH 1 INCREMENT BY 50;


CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY DEFAULT nextval('users_seq'),
                                     name VARCHAR(100) NOT NULL,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     age INTEGER CHECK (age >= 0 AND age <= 120),
                                     created_at TIMESTAMP NOT NULL,
                                     updated_at TIMESTAMP NOT NULL
);

CREATE INDEX IF NOT EXISTS idx_users_created_at ON users(created_at);