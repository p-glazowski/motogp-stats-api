CREATE TABLE tracks (
    id                BIGSERIAL PRIMARY KEY,
    gp_name           VARCHAR(255),
    circuit_name      VARCHAR(255) NOT NULL,
    country           VARCHAR(100) NOT NULL,
    city              VARCHAR(100),
    length_km         DOUBLE PRECISION,
    number_of_turns   INTEGER,
    race_laps         INTEGER,
    lap_record_time   VARCHAR(20),
    lap_record_holder VARCHAR(255),
    lap_record_year   INTEGER,
    first_held_year   INTEGER
);