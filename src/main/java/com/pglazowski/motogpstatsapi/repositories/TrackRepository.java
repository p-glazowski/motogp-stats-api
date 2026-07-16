package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackRepository extends JpaRepository<Track, Long> {
}
