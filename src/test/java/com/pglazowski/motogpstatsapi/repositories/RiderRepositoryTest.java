package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Rider;
import com.pglazowski.motogpstatsapi.models.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
class RiderRepositoryTest {

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Team ducatiTeam;
    private Team yamahaTeam;
    private Rider rider1;
    private Rider rider2;
    private Rider rider3;
    private Rider rider4;

    @BeforeEach
    void setUp() {
        riderRepository.deleteAll();
        teamRepository.deleteAll();

        // Create teams
        ducatiTeam = new Team(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        yamahaTeam = new Team(
                "Monster Energy Yamaha MotoGP",
                "Yamaha",
                "Japan",
                "Massimo Meregalli",
                "Lesmo, Italy",
                1999,
                "https://www.yamaha-racing.com"
        );

        teamRepository.saveAll(List.of(ducatiTeam, yamahaTeam));

        // Create riders
        rider1 = new Rider(
                "Francesco",
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                63,
                ducatiTeam,
                25,
                45,
                18,
                2,
                "Two-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/bagnaia.jpg"
        );

        rider2 = new Rider(
                "Marc",
                "Marquez",
                "Marc Marquez",
                "Spain",
                33,
                93,
                ducatiTeam,
                64,
                101,
                64,
                6,
                "Six-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/marquez.jpg"
        );

        rider3 = new Rider(
                "Fabio",
                "Quartararo",
                "Fabio Quartararo",
                "France",
                26,
                20,
                yamahaTeam,
                11,
                23,
                16,
                1,
                "2021 MotoGP World Champion.",
                "https://www.motogp.com/images/riders/quartararo.jpg"
        );

        rider4 = new Rider(
                "Alex",
                "Rins",
                "Alex Rins",
                "Spain",
                30,
                42,
                yamahaTeam,
                5,
                12,
                4,
                0,
                "Known for smooth riding style.",
                "https://www.motogp.com/images/riders/rins.jpg"
        );

        riderRepository.saveAll(List.of(rider1, rider2, rider3, rider4));
    }

    @Test
    void findByNationalityIgnoreCase_ShouldReturnRidersWithMatchingNationality() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rider> spanishRiders = riderRepository.findByNationalityIgnoreCase("spain", pageable);

        assertThat(spanishRiders).isNotEmpty();
        assertThat(spanishRiders.getContent()).hasSize(2);
        assertThat(spanishRiders.getContent())
                .extracting(Rider::getNationality)
                .allMatch(nationality -> nationality.equalsIgnoreCase("spain"));
        assertThat(spanishRiders.getContent())
                .extracting(Rider::getFullName)
                .containsExactlyInAnyOrder("Marc Marquez", "Alex Rins");
    }

    @Test
    void findByNationalityIgnoreCase_ShouldReturnEmpty_WhenNoMatchingNationality() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rider> germanRiders = riderRepository.findByNationalityIgnoreCase("germany", pageable);

        assertThat(germanRiders).isEmpty();
        assertThat(germanRiders.getContent()).isEmpty();
        assertThat(germanRiders.getTotalElements()).isZero();
    }

    @Test
    void findByTeamId_ShouldReturnRidersForTeam() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rider> ducatiRiders = riderRepository.findByTeamId(ducatiTeam.getId(), pageable);

        assertThat(ducatiRiders).isNotEmpty();
        assertThat(ducatiRiders.getContent()).hasSize(2);
        assertThat(ducatiRiders.getContent())
                .extracting(Rider::getTeam)
                .allMatch(team -> team.getId().equals(ducatiTeam.getId()));
        assertThat(ducatiRiders.getContent())
                .extracting(Rider::getFullName)
                .containsExactlyInAnyOrder("Francesco Bagnaia", "Marc Marquez");
    }

    @Test
    void findByTeamId_ShouldReturnEmpty_WhenTeamHasNoRiders() {
        Pageable pageable = PageRequest.of(0, 10);
        Long nonExistentTeamId = 999L;
        Page<Rider> riders = riderRepository.findByTeamId(nonExistentTeamId, pageable);

        assertThat(riders).isEmpty();
        assertThat(riders.getContent()).isEmpty();
        assertThat(riders.getTotalElements()).isZero();
    }

    @Test
    void findByFullNameContainingIgnoreCase_ShouldReturnMatchingRiders() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rider> riders = riderRepository.findByFullNameContainingIgnoreCase("mar", pageable);

        assertThat(riders).isNotEmpty();
        assertThat(riders.getContent()).hasSize(1);
        assertThat(riders.getContent())
                .extracting(Rider::getFullName)
                .containsExactlyInAnyOrder("Marc Marquez");
    }

    @Test
    void findByFullNameContainingIgnoreCase_ShouldReturnEmpty_WhenNoMatch() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Rider> riders = riderRepository.findByFullNameContainingIgnoreCase("xyz", pageable);

        assertThat(riders).isEmpty();
        assertThat(riders.getContent()).isEmpty();
        assertThat(riders.getTotalElements()).isZero();
    }

    @Test
    void findByFullNameIgnoreCase_ShouldReturnRider_WhenExactMatch() {
        Optional<Rider> foundRider = riderRepository.findByFullNameIgnoreCase("francesco bagnaia");

        assertThat(foundRider).isPresent();
        assertThat(foundRider.get().getFullName()).isEqualTo("Francesco Bagnaia");
        assertThat(foundRider.get().getNationality()).isEqualTo("Italy");
        assertThat(foundRider.get().getTeam().getName()).isEqualTo("Ducati Lenovo Team");
    }

    @Test
    void findByFullNameIgnoreCase_ShouldReturnRider_WhenCaseInsensitive() {
        Optional<Rider> foundRider = riderRepository.findByFullNameIgnoreCase("MARC MARQUEZ");

        assertThat(foundRider).isPresent();
        assertThat(foundRider.get().getFullName()).isEqualTo("Marc Marquez");
    }

    @Test
    void findByFullNameIgnoreCase_ShouldReturnEmpty_WhenNoMatch() {
        Optional<Rider> foundRider = riderRepository.findByFullNameIgnoreCase("Non Existent Rider");

        assertThat(foundRider).isEmpty();
    }

    @Test
    void existsByFullNameIgnoreCase_ShouldReturnTrue_WhenRiderExists() {
        boolean exists = riderRepository.existsByFullNameIgnoreCase("fabio quartararo");

        assertThat(exists).isTrue();
    }

    @Test
    void existsByFullNameIgnoreCase_ShouldReturnFalse_WhenRiderDoesNotExist() {
        boolean exists = riderRepository.existsByFullNameIgnoreCase("non existent rider");

        assertThat(exists).isFalse();
    }

    @Test
    void existsByRaceNumber_ShouldReturnTrue_WhenRaceNumberExists() {
        boolean exists = riderRepository.existsByRaceNumber(63);

        assertThat(exists).isTrue();
    }

    @Test
    void existsByRaceNumber_ShouldReturnFalse_WhenRaceNumberDoesNotExist() {
        boolean exists = riderRepository.existsByRaceNumber(99);

        assertThat(exists).isFalse();
    }

    @Test
    void findAllDistinctNationalities_ShouldReturnAllUniqueNationalities() {
        List<String> nationalities = riderRepository.findAllDistinctNationalities();

        assertThat(nationalities).isNotEmpty();
        assertThat(nationalities).hasSize(3);
        assertThat(nationalities).containsExactly("France", "Italy", "Spain");
        assertThat(nationalities).isSorted(); // Should be sorted alphabetically
    }

    @Test
    void save_ShouldPersistRider() {
        Rider newRider = new Rider(
                "Jorge",
                "Martin",
                "Jorge Martin",
                "Spain",
                27,
                89,
                ducatiTeam,
                3,
                9,
                5,
                0,
                "Promising young talent.",
                "https://www.motogp.com/images/riders/martin.jpg"
        );

        Rider savedRider = riderRepository.save(newRider);

        assertThat(savedRider.getId()).isNotNull();
        assertThat(savedRider.getFullName()).isEqualTo("Jorge Martin");
        assertThat(savedRider.getNationality()).isEqualTo("Spain");
        assertThat(savedRider.getTeam().getId()).isEqualTo(ducatiTeam.getId());

        // Verify it was actually saved
        Optional<Rider> foundRider = riderRepository.findById(savedRider.getId());
        assertThat(foundRider).isPresent();
        assertThat(foundRider.get().getFullName()).isEqualTo("Jorge Martin");
    }

    @Test
    void delete_ShouldRemoveRider() {
        Rider riderToDelete = riderRepository.findByFullNameIgnoreCase("alex rins").orElseThrow();
        Long riderId = riderToDelete.getId();

        riderRepository.delete(riderToDelete);

        Optional<Rider> deletedRider = riderRepository.findById(riderId);
        assertThat(deletedRider).isEmpty();
        assertThat(riderRepository.count()).isEqualTo(3);
    }

    @Test
    void count_ShouldReturnTotalRiders() {
        long count = riderRepository.count();
        assertThat(count).isEqualTo(4);
    }

    @Test
    void findById_ShouldReturnRider_WhenExists() {
        Rider savedRider = riderRepository.findByFullNameIgnoreCase("marc marquez").orElseThrow();
        Optional<Rider> foundRider = riderRepository.findById(savedRider.getId());

        assertThat(foundRider).isPresent();
        assertThat(foundRider.get().getFullName()).isEqualTo("Marc Marquez");
        assertThat(foundRider.get().getWins()).isEqualTo(64);
        assertThat(foundRider.get().getWorldTitles()).isEqualTo(6);
    }

    @Test
    void findById_ShouldReturnEmpty_WhenNotExists() {
        Optional<Rider> foundRider = riderRepository.findById(999L);
        assertThat(foundRider).isEmpty();
    }
}