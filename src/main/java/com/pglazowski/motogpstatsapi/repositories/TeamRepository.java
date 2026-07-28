package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Team;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findByCountryIgnoreCase(String country, Pageable pageable);

    Page<Team> findByManufacturerIgnoreCase(String manufacturer, Pageable pageable);

    Page<Team> findByCountryIgnoreCaseAndManufacturerIgnoreCase(
            String country,
            String manufacturer,
            Pageable pageable
    );

    Page<Team> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Optional<Team> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    @Query("SELECT DISTINCT t.country FROM Team t ORDER BY t.country")
    List<String> findAllDistinctCountries();

    @Query("SELECT DISTINCT t.manufacturer FROM Team t ORDER BY t.manufacturer")
    List<String> findAllDistinctManufacturers();

    Page<Team> findByFoundedYearBetween(Integer startYear, Integer endYear, Pageable pageable);
}
