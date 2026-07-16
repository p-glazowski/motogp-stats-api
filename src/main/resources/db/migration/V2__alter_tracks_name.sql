ALTER TABLE tracks
    ADD CONSTRAINT uq_tracks_name UNIQUE (name);