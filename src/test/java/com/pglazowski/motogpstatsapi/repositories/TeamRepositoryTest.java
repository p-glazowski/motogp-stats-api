package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class TeamRepositoryTest {

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();

        // Create test teams based on 2026 MotoGP grid
        Team ducatiTeam = new Team(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        Team yamahaTeam = new Team(
                "Monster Energy Yamaha MotoGP",
                "Yamaha",
                "Japan",
                "Massimo Meregalli",
                "Lesmo, Italy",
                1999,
                "https://www.yamaha-racing.com"
        );

        Team hondaTeam = new Team(
                "Honda HRC Castrol",
                "Honda",
                "Japan",
                "Alberto Puig",
                "Tokyo, Japan",
                1995,
                "https://www.honda.racing"
        );

        Team ktmTeam = new Team(
                "Red Bull KTM Factory Racing",
                "KTM",
                "Austria",
                "Francesco Guidotti",
                "Munderfing, Austria",
                2017,
                "https://www.ktm.com/racing"
        );

        Team apriliaTeam = new Team(
                "Aprilia Racing",
                "Aprilia",
                "Italy",
                "Massimo Rivola",
                "Noale, Italy",
                2015,
                "https://www.aprilia.com/racing"
        );

        Team suzukiTeam = new Team(
                "Team Suzuki Ecstar",
                "Suzuki",
                "Japan",
                "Shinichi Sahara",
                "Hamamatsu, Japan",
                2015,
                "https://www.suzuki-racing.com"
        );

        Team pramacYamahaTeam = new Team(
                "Prima Pramac Yamaha MotoGP",
                "Yamaha",
                "Italy",
                "Paolo Campinoti",
                "Lugano, Switzerland",
                2002,
                "https://www.pramacracing.com"
        );

        teamRepository.saveAll(List.of(
                ducatiTeam, yamahaTeam, hondaTeam, ktmTeam,
                apriliaTeam, suzukiTeam, pramacYamahaTeam
        ));
    }

    @Test
    void testFindByCountryIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> italianTeams = teamRepository.findByCountryIgnoreCase("italy", pageable);

        assertThat(italianTeams).isNotEmpty();
        assertThat(italianTeams.getContent()).hasSize(3);
        assertThat(italianTeams.getContent())
                .extracting(Team::getCountry)
                .allMatch(country -> country.equalsIgnoreCase("italy"));
        assertThat(italianTeams.getContent())
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(
                        "Ducati Lenovo Team",
                        "Aprilia Racing",
                        "Prima Pramac Yamaha MotoGP"
                );
    }

    @Test
    void testFindByCountryIgnoreCase_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> spanishTeams = teamRepository.findByCountryIgnoreCase("spain", pageable);

        assertThat(spanishTeams).isEmpty();
        assertThat(spanishTeams.getContent()).isEmpty();
        assertThat(spanishTeams.getTotalElements()).isZero();
    }

    @Test
    void testFindByCountryIgnoreCase_WithPagination() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<Team> italianTeams = teamRepository.findByCountryIgnoreCase("italy", pageable);

        assertThat(italianTeams.getContent()).hasSize(2);
        assertThat(italianTeams.getTotalElements()).isEqualTo(3);
        assertThat(italianTeams.getTotalPages()).isEqualTo(2);
        assertThat(italianTeams.isFirst()).isTrue();
        assertThat(italianTeams.hasNext()).isTrue();
    }

    @Test
    void testFindByManufacturerIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> yamahaTeams = teamRepository.findByManufacturerIgnoreCase("yamaha", pageable);

        assertThat(yamahaTeams).isNotEmpty();
        assertThat(yamahaTeams.getContent()).hasSize(2);
        assertThat(yamahaTeams.getContent())
                .extracting(Team::getManufacturer)
                .allMatch(manufacturer -> manufacturer.equalsIgnoreCase("yamaha"));
        assertThat(yamahaTeams.getContent())
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(
                        "Monster Energy Yamaha MotoGP",
                        "Prima Pramac Yamaha MotoGP"
                );
    }

    @Test
    void testFindByManufacturerIgnoreCase_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> kawasakiTeams = teamRepository.findByManufacturerIgnoreCase("kawasaki", pageable);

        assertThat(kawasakiTeams).isEmpty();
        assertThat(kawasakiTeams.getContent()).isEmpty();
        assertThat(kawasakiTeams.getTotalElements()).isZero();
    }

    @Test
    void testFindByCountryIgnoreCaseAndManufacturerIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> italianDucatiTeams = teamRepository.findByCountryIgnoreCaseAndManufacturerIgnoreCase(
                "italy", "ducati", pageable);

        assertThat(italianDucatiTeams).isNotEmpty();
        assertThat(italianDucatiTeams.getContent()).hasSize(1);
        assertThat(italianDucatiTeams.getContent().get(0).getCountry())
                .isEqualToIgnoringCase("italy");
        assertThat(italianDucatiTeams.getContent().get(0).getManufacturer())
                .isEqualToIgnoringCase("ducati");
        assertThat(italianDucatiTeams.getContent().get(0).getName())
                .isEqualTo("Ducati Lenovo Team");
    }

    @Test
    void testFindByCountryIgnoreCaseAndManufacturerIgnoreCase_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> italianHondaTeams = teamRepository.findByCountryIgnoreCaseAndManufacturerIgnoreCase(
                "italy", "honda", pageable);

        assertThat(italianHondaTeams).isEmpty();
        assertThat(italianHondaTeams.getContent()).isEmpty();
        assertThat(italianHondaTeams.getTotalElements()).isZero();
    }

    @Test
    void testFindByNameContainingIgnoreCase() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamsWithRacing = teamRepository.findByNameContainingIgnoreCase("racing", pageable);

        assertThat(teamsWithRacing).isNotEmpty();
        assertThat(teamsWithRacing.getContent()).hasSize(2);
        assertThat(teamsWithRacing.getContent())
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(
                        "Aprilia Racing",
                        "Red Bull KTM Factory Racing"
                );
    }

    @Test
    void testFindByNameContainingIgnoreCase_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamsWithNoMatch = teamRepository.findByNameContainingIgnoreCase("xyz", pageable);

        assertThat(teamsWithNoMatch).isEmpty();
        assertThat(teamsWithNoMatch.getContent()).isEmpty();
        assertThat(teamsWithNoMatch.getTotalElements()).isZero();
    }

    @Test
    void testFindByNameIgnoreCase() {
        Optional<Team> foundTeam = teamRepository.findByNameIgnoreCase("ducati lenovo team");

        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo("Ducati Lenovo Team");
        assertThat(foundTeam.get().getManufacturer()).isEqualTo("Ducati");
        assertThat(foundTeam.get().getCountry()).isEqualTo("Italy");
    }

    @Test
    void testFindByNameIgnoreCase_CaseInsensitive() {
        Optional<Team> foundTeam = teamRepository.findByNameIgnoreCase("HONDA HRC CASTROL");

        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo("Honda HRC Castrol");
        assertThat(foundTeam.get().getManufacturer()).isEqualTo("Honda");
    }

    @Test
    void testFindByNameIgnoreCase_NotFound() {
        Optional<Team> foundTeam = teamRepository.findByNameIgnoreCase("Non Existing Team");
        assertThat(foundTeam).isEmpty();
    }

    @Test
    void testExistsByNameIgnoreCase() {
        boolean exists = teamRepository.existsByNameIgnoreCase("ducati lenovo team");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNameIgnoreCase_CaseInsensitive() {
        boolean exists = teamRepository.existsByNameIgnoreCase("RED BULL KTM FACTORY RACING");
        assertThat(exists).isTrue();
    }

    @Test
    void testExistsByNameIgnoreCase_NotFound() {
        boolean exists = teamRepository.existsByNameIgnoreCase("non existing team");
        assertThat(exists).isFalse();
    }

    @Test
    void testFindAllDistinctCountries() {
        List<String> countries = teamRepository.findAllDistinctCountries();

        assertThat(countries).isNotEmpty();
        assertThat(countries).hasSize(3);
        assertThat(countries).containsExactlyInAnyOrder("Austria", "Italy", "Japan");
    }

    @Test
    void testFindAllDistinctManufacturers() {
        List<String> manufacturers = teamRepository.findAllDistinctManufacturers();

        assertThat(manufacturers).isNotEmpty();
        assertThat(manufacturers).hasSize(6);
        assertThat(manufacturers).containsExactlyInAnyOrder("Aprilia", "Ducati", "Honda", "KTM", "Suzuki", "Yamaha");
    }

    @Test
    void testFindByFoundedYearBetween() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamsFounded1995to2000 = teamRepository.findByFoundedYearBetween(1995, 2000, pageable);

        assertThat(teamsFounded1995to2000).isNotEmpty();
        assertThat(teamsFounded1995to2000.getContent()).hasSize(3);
        assertThat(teamsFounded1995to2000.getContent())
                .extracting(Team::getName)
                .containsExactlyInAnyOrder(
                        "Ducati Lenovo Team",
                        "Monster Energy Yamaha MotoGP",
                        "Honda HRC Castrol"
                );
    }

    @Test
    void testFindByFoundedYearBetween_NoResults() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Team> teamsFounded2025to2030 = teamRepository.findByFoundedYearBetween(2025, 2030, pageable);

        assertThat(teamsFounded2025to2030).isEmpty();
        assertThat(teamsFounded2025to2030.getContent()).isEmpty();
        assertThat(teamsFounded2025to2030.getTotalElements()).isZero();
    }

    @Test
    void testFindByFoundedYearBetween_WithSortingAscending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("foundedYear").ascending());
        Page<Team> teams = teamRepository.findByFoundedYearBetween(1995, 2020, pageable);

        assertThat(teams).isNotEmpty();
        assertThat(teams.getContent().get(0).getFoundedYear()).isEqualTo(1995); // Honda
        assertThat(teams.getContent().get(1).getFoundedYear()).isEqualTo(1999); // Ducati/Yamaha
    }

    @Test
    void testFindByFoundedYearBetween_WithSortingDescending() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("foundedYear").descending());
        Page<Team> teams = teamRepository.findByFoundedYearBetween(1995, 2020, pageable);

        assertThat(teams).isNotEmpty();
        assertThat(teams.getContent().get(0).getFoundedYear()).isEqualTo(2017); // KTM
    }

    @Test
    void testSaveTeam() {
        Team newTeam = new Team(
                "VR46 Racing Team",
                "Ducati",
                "Italy",
                "Alessio Salucci",
                "Tavullia, Italy",
                2014,
                "https://www.vr46.it"
        );

        Team savedTeam = teamRepository.save(newTeam);

        assertThat(savedTeam.getId()).isNotNull();
        assertThat(savedTeam.getName()).isEqualTo("VR46 Racing Team");
        assertThat(savedTeam.getManufacturer()).isEqualTo("Ducati");
        assertThat(savedTeam.getCountry()).isEqualTo("Italy");
        assertThat(savedTeam.getFoundedYear()).isEqualTo(2014);
    }

    @Test
    void testUpdateTeam() {
        Team teamToUpdate = teamRepository.findByNameIgnoreCase("ducati lenovo team").orElseThrow();
        teamToUpdate.setCountry("Italy (Updated)");

        Team updatedTeam = teamRepository.save(teamToUpdate);

        assertThat(updatedTeam.getCountry()).isEqualTo("Italy (Updated)");
    }

    @Test
    void testDeleteTeam() {
        Team teamToDelete = teamRepository.findByNameIgnoreCase("aprilia racing").orElseThrow();
        Long teamId = teamToDelete.getId();

        teamRepository.delete(teamToDelete);

        Optional<Team> deletedTeam = teamRepository.findById(teamId);
        assertThat(deletedTeam).isEmpty();
        assertThat(teamRepository.count()).isEqualTo(6);
    }

    @Test
    void testCountTeams() {
        long count = teamRepository.count();
        assertThat(count).isEqualTo(7);
    }

    @Test
    void testFindById_Existing() {
        Team savedTeam = teamRepository.findByNameIgnoreCase("honda hrc castrol").orElseThrow();
        Optional<Team> foundTeam = teamRepository.findById(savedTeam.getId());

        assertThat(foundTeam).isPresent();
        assertThat(foundTeam.get().getName()).isEqualTo("Honda HRC Castrol");
    }

    @Test
    void testFindById_NonExisting() {
        Optional<Team> foundTeam = teamRepository.findById(999L);
        assertThat(foundTeam).isEmpty();
    }
}