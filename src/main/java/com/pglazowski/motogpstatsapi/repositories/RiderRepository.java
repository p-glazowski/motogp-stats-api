package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Rider;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RiderRepository extends JpaRepository<Rider, Long> {

    // Find by nationality with pagination
    Page<Rider> findByNationalityIgnoreCase(String nationality, Pageable pageable);

    // Find by team ID with pagination
    Page<Rider> findByTeamId(Long teamId, Pageable pageable);

    // Search by full name (partial match, case-insensitive)
    Page<Rider> findByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    // Find exact match by full name (case-insensitive)
    Optional<Rider> findByFullNameIgnoreCase(String fullName);

    // Check if rider exists by full name
    boolean existsByFullNameIgnoreCase(String fullName);

    // Check if race number is already taken
    boolean existsByRaceNumber(Integer raceNumber);

    // Get all distinct nationalities (for filter dropdowns)
    @Query("SELECT DISTINCT r.nationality FROM Rider r ORDER BY r.nationality")
    List<String> findAllDistinctNationalities();

    Page<Rider> findByNationalityIgnoreCaseAndTeamId(String nationality, Long teamId, Pageable pageable);

    Page<Rider> findByNationalityIgnoreCaseAndFullNameContainingIgnoreCase(
            String nationality, String fullName, Pageable pageable);

    Page<Rider> findByTeamIdAndFullNameContainingIgnoreCase(Long teamId, String fullName, Pageable pageable);

    Page<Rider> findByNationalityIgnoreCaseAndTeamIdAndFullNameContainingIgnoreCase(
            String nationality, Long teamId, String fullName, Pageable pageable);

}
