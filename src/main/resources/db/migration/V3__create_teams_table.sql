CREATE TABLE teams (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE,
    manufacturer VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL,
    team_principal VARCHAR(255),
    base_location VARCHAR(255),
    founded_year INTEGER,
    website VARCHAR
);