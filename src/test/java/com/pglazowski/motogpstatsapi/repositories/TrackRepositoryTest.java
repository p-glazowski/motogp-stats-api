package com.pglazowski.motogpstatsapi.repositories;

import com.pglazowski.motogpstatsapi.models.Track;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
public class TrackRepositoryTest {

    @Autowired
    private TrackRepository trackRepository;


    @Test
    void findByCircuitName_returnsTrack() {
        Track track = new Track(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                null,
                null,
                null,
                1974
        );

        trackRepository.save(track);

        Optional<Track> result = trackRepository.findByCircuitName("Mugello");

        assertThat(result).isPresent();
        assertThat(result.get().getCountry()).isEqualTo("Italy");
    }

    @Test
    void existsByCircuitName_returnsTrue() {
        Track track = new Track(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                null,
                null,
                null,
                1974
        );

        trackRepository.save(track);

        assertThat(trackRepository.existsByCircuitName("Mugello"))
                .isTrue();
    }

    @Test
    void findByCountry_returnsMatchingTracks() {
        trackRepository.save(new Track(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                null,
                null,
                null,
                1974
        ));

        trackRepository.save(new Track(
                "San Marino GP",
                "Misano",
                "Italy",
                "Misano",
                4.226,
                16,
                27,
                null,
                null,
                null,
                1980
        ));

        trackRepository.save(new Track(
                "Spanish GP",
                "Jerez",
                "Spain",
                "Jerez",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        ));

        List<Track> result = trackRepository.findByCountry("Italy");

        assertEquals(2, result.size());
    }

    @Test
    void findAllByOrderByLengthKmDesc_returnsTracksSortedDescending() {
        trackRepository.save(new Track(
                "A",
                "Long Circuit",
                "Country",
                "City",
                6.0,
                10,
                20,
                null,
                null,
                null,
                2000
        ));

        trackRepository.save(new Track(
                "B",
                "Short Circuit",
                "Country",
                "City",
                4.0,
                10,
                20,
                null,
                null,
                null,
                2000
        ));

        List<Track> result = trackRepository.findAllByOrderByLengthKmDesc();

        assertEquals(6.0, result.get(0).getLengthKm());
        assertEquals(4.0, result.get(1).getLengthKm());
    }

    @Test
    void findByCountryIgnoreCase_returnsMatchingTracks() {
        trackRepository.save(new Track(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                null,
                null,
                null,
                1974
        ));

        trackRepository.save(new Track(
                "San Marino GP",
                "Misano",
                "ITALY",
                "Misano",
                4.226,
                16,
                27,
                null,
                null,
                null,
                1980
        ));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Track> result =
                trackRepository.findByCountryIgnoreCase("italy", pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findByCityIgnoreCase_returnsMatchingTracks() {
        trackRepository.save(new Track(
                "Spanish GP",
                "Jerez",
                "Spain",
                "Jerez",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        ));

        trackRepository.save(new Track(
                "Another GP",
                "Test Circuit",
                "Spain",
                "JEREZ",
                5.0,
                10,
                20,
                null,
                null,
                null,
                2000
        ));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Track> result =
                trackRepository.findByCityIgnoreCase("jerez", pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void findByCountryIgnoreCaseAndCityIgnoreCase_returnsMatchingTracks() {
        trackRepository.save(new Track(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                null,
                null,
                null,
                1974
        ));

        trackRepository.save(new Track(
                "Another GP",
                "Test Circuit",
                "Italy",
                "Monza",
                5.8,
                11,
                20,
                null,
                null,
                null,
                2000
        ));

        Pageable pageable = PageRequest.of(0, 10);

        Page<Track> result =
                trackRepository.findByCountryIgnoreCaseAndCityIgnoreCase(
                        "ITALY",
                        "SCARPERIA",
                        pageable
                );

        assertEquals(1, result.getTotalElements());
        assertEquals("Mugello", result.getContent().get(0).getCircuitName());
    }

    @Test
    void findByCountryIgnoreCase_respectsPagination() {
        trackRepository.save(new Track(
                "GP1", "Circuit1", "Italy", "City1",
                5.0, 10, 20, null, null, null, 2000
        ));

        trackRepository.save(new Track(
                "GP2", "Circuit2", "Italy", "City2",
                5.1, 10, 20, null, null, null, 2001
        ));

        Pageable pageable = PageRequest.of(0, 1);

        Page<Track> result =
                trackRepository.findByCountryIgnoreCase("Italy", pageable);

        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
    }


}
