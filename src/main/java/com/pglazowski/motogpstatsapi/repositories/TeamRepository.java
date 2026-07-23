package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.models.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Page<Team> findByCountryIgnoreCase(String country, Pageable pageable);

    Page<Team> findByManufacturerIgnoreCase(String manufacturer, Pageable pageable);

    Page<Team> findByCountryIgnoreCaseAndManufacturerIgnoreCase(
            String country,
            String manufacturer,
            Pageable pageable
    );
}
