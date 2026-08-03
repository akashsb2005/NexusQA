CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL
);

INSERT INTO users (email, name)
VALUES ('standard_user@example.com', 'Standard User')
ON CONFLICT (email) DO NOTHING;