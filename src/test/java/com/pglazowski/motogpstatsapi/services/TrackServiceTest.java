package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TrackServiceTest {

    @Mock
    private TrackRepository trackRepository;

    @InjectMocks
    private TrackService trackService;

    @Test
    public void getAllTracks_returnsMappedTrackResponses() {
        Track track = new Track(
                "Grand Prix of Spain",
                "Circuito de Jerez – Ángel Nieto",
                "Spain",
                "Jerez de la Frontera",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        );

        List<Track> allTracks = List.of(track);

        when(trackRepository.findAll())
                .thenReturn(allTracks);

        List<TrackResponse> result = trackService.getAllTracks();

        assertEquals(1, result.size());
        assertThat(result.get(0).circuitName()).isEqualTo("Circuito de Jerez – Ángel Nieto");
        assertThat(result.get(0).country()).isEqualTo("Spain");
        assertThat(result.get(0).lengthKm()).isEqualTo(4.423);

        verify(trackRepository).findAll();
    }

    @Test
    public void getAllTracks_returnsEmptyList_whenNoTracksExist() {
        List<Track> tracks = List.of();

        when(trackRepository.findAll())
                .thenReturn(tracks);

        List<TrackResponse> result = trackService.getAllTracks();

        assertEquals(0, result.size());

        verify(trackRepository).findAll();
    }

    @Test
    public void getTrackById_returnsTrackWhenFound() {
        Track track1 = new Track(
                "Grand Prix of Spain",
                "Circuito de Jerez – Ángel Nieto",
                "Spain",
                "Jerez de la Frontera",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        );

        Long trackId = 1L;

        when(trackRepository.findById(trackId))
                .thenReturn(Optional.of(track1));

        TrackResponse result = trackService.getTrackById(trackId);

        assertThat(result.gpName()).isEqualTo("Grand Prix of Spain");
        verify(trackRepository).findById(trackId);
    }

    @Test
    public void getTracById_throwsNotFoundExceptionWhenMissing() {
        Long trackId = 9999L;

        when(trackRepository.findById(trackId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.getTrackById(trackId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Track id " + trackId + " not found");

        verify(trackRepository).findById(trackId);
    }

    @Test
    void getTrackByCircuitName_returnsMappedTrackWhenFound() {
        Track track = new Track(
                "Grand Prix of Spain",
                "Circuito de Jerez – Ángel Nieto",
                "Spain",
                "Jerez de la Frontera",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        );

        when(trackRepository.findByCircuitName("Circuito de Jerez – Ángel Nieto"))
                .thenReturn(Optional.of(track));

        TrackResponse result = trackService.getTrackByCircuitName("Circuito de Jerez – Ángel Nieto");

        assertThat(result.circuitName()).isEqualTo("Circuito de Jerez – Ángel Nieto");
        verify(trackRepository).findByCircuitName("Circuito de Jerez – Ángel Nieto");
    }

    @Test
    void getTracksByCountry_returnsMappedTracks() {
        Track track = new Track(
                "Grand Prix of Spain",
                "Circuito de Jerez – Ángel Nieto",
                "Spain",
                "Jerez de la Frontera",
                4.423,
                13,
                25,
                null,
                null,
                null,
                1987
        );

        when(trackRepository.findByCountry("Spain")).thenReturn(List.of(track));

        List<TrackResponse> result = trackService.getTracksByCountry("Spain");

        assertEquals(1, result.size());
        assertThat(result.get(0).country()).isEqualTo("Spain");
        verify(trackRepository).findByCountry("Spain");
    }

    @Test
    void trackExistsByCircuitName_returnsTrue() {
        when(trackRepository.existsByCircuitName("Circuito de Jerez – Ángel Nieto"))
                .thenReturn(true);

        boolean result = trackService.trackExistsByCircuitName("Circuito de Jerez – Ángel Nieto");

        assertThat(result).isTrue();
        verify(trackRepository).existsByCircuitName("Circuito de Jerez – Ángel Nieto");
    }

    @Test
    void getTracksSortedByLengthDesc_returnsSortedMappedTracks() {
        Track track1 = new Track("A", "Circuit A", "Country A", "City A", 5.0, 10, 20, null, null, null, 2000);
        Track track2 = new Track("B", "Circuit B", "Country B", "City B", 4.0, 11, 21, null, null, null, 2001);

        when(trackRepository.findAllByOrderByLengthKmDesc()).thenReturn(List.of(track1, track2));

        List<TrackResponse> result = trackService.getTracksSortedByLengthDesc();

        assertEquals(2, result.size());
        assertThat(result.get(0).lengthKm()).isEqualTo(5.0);
        assertThat(result.get(1).lengthKm()).isEqualTo(4.0);
        verify(trackRepository).findAllByOrderByLengthKmDesc();
    }

    @Test
    void createTrack_savesAndReturnsTrack() {
        TrackRequest request = new TrackRequest(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                "1:45.187",
                "Francesco Bagnaia",
                2023,
                1974
        );

        Track savedTrack = new Track(
                request.gpName(),
                request.circuitName(),
                request.country(),
                request.city(),
                request.lengthKm(),
                request.numberOfTurns(),
                request.raceLaps(),
                request.lapRecordTime(),
                request.lapRecordHolder(),
                request.lapRecordYear(),
                request.firstHeldYear()
        );

        when(trackRepository.save(any(Track.class)))
                .thenReturn(savedTrack);

        TrackResponse result = trackService.createTrack(request);

        assertThat(result.gpName()).isEqualTo("Italian GP");
        assertThat(result.circuitName()).isEqualTo("Mugello");

        verify(trackRepository).save(any(Track.class));
    }

    @Test
    void updateTrack_updatesExistingTrack() {
        Long id = 1L;

        Track existing = new Track(
                "Old GP",
                "Old Circuit",
                "Old Country",
                "Old City",
                4.0,
                10,
                20,
                null,
                null,
                null,
                1990
        );

        TrackRequest request = new TrackRequest(
                "Italian GP",
                "Mugello",
                "Italy",
                "Scarperia",
                5.245,
                15,
                23,
                "1:45.187",
                "Francesco Bagnaia",
                2023,
                1974
        );

        when(trackRepository.findById(id))
                .thenReturn(Optional.of(existing));

        when(trackRepository.save(existing))
                .thenReturn(existing);

        TrackResponse result = trackService.updateTrack(id, request);

        assertThat(result.gpName()).isEqualTo("Italian GP");
        assertThat(result.circuitName()).isEqualTo("Mugello");
        assertThat(result.country()).isEqualTo("Italy");
        assertThat(result.city()).isEqualTo("Scarperia");

        verify(trackRepository).findById(id);
        verify(trackRepository).save(existing);
    }

    @Test
    void updateTrack_throwsNotFoundWhenTrackDoesNotExist() {
        Long id = 100L;

        TrackRequest request = new TrackRequest(
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

        when(trackRepository.findById(id))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> trackService.updateTrack(id, request))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Track id " + id + " not found");

        verify(trackRepository).findById(id);
    }

    @Test
    void deleteTrack_deletesExistingTrack() {
        Long id = 1L;

        when(trackRepository.existsById(id))
                .thenReturn(true);

        trackService.deleteTrack(id);

        verify(trackRepository).existsById(id);
        verify(trackRepository).deleteById(id);
    }

    @Test
    void deleteTrack_throwsNotFoundWhenTrackDoesNotExist() {
        Long id = 999L;

        when(trackRepository.existsById(id))
                .thenReturn(false);

        assertThatThrownBy(() -> trackService.deleteTrack(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Track id " + id + " not found");

        verify(trackRepository).existsById(id);
        verify(trackRepository, never()).deleteById(anyLong());
    }
}
