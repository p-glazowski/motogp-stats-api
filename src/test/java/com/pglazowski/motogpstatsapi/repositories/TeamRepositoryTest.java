package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.models.Track;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class TeamRepositoryTest {

    @Autowired
    TeamRepository teamRepository;

    Team testTeam = new Team(
            "Ducati Lenovo Team",
            "Ducati",
            "Italy",
            "Luigi Dall'Igna",
            "Bologna, Italy",
            1999,
            "https://ducati.com/racing"
    );

    @Test
    void findByCountryIgnoreCase_returnsMatchingTeams() {
        Team team = testTeam;

        teamRepository.save(team);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Team> result =
                teamRepository.findByCountryIgnoreCase("italy", pageable);

        assertEquals(1, result.getTotalElements());

    }
}
