CREATE TABLE riders (
    id BIGSERIAL PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    full_name VARCHAR(200) NOT NULL UNIQUE,
    nationality VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL CHECK (age >= 16 AND age <= 65),
    race_number INTEGER UNIQUE,
    team_id BIGINT,
    wins INTEGER DEFAULT 0,
    podiums INTEGER DEFAULT 0,
    pole_positions INTEGER DEFAULT 0,
    world_titles INTEGER DEFAULT 0,
    biography TEXT,
    image_url VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_rider_team FOREIGN KEY (team_id) REFERENCES teams(id) ON DELETE SET NULL
    );