CREATE TABLE persons (
    id UUID PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    dob VARCHAR(10) NOT NULL,
    created_at TIMESTAMP NOT NULL
);