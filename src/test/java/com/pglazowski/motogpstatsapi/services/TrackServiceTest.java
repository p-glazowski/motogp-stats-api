package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TrackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
}
