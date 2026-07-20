package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TrackRepository extends JpaRepository<Track, Long> {
    Optional<Track> findByCircuitName(String circuitName);

    List<Track> findByCountry(String country);

    boolean existsByCircuitName(String circuitName);

    List<Track> findAllByOrderByLengthKmDesc();
}
