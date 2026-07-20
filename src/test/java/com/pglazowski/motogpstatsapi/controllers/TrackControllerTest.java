package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.services.TrackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TrackController.class)
public class TrackControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TrackService trackService;

    @Test
    void getAllTracks_returnsListOfTracks() throws Exception {
        TrackResponse track1 = new TrackResponse(
                1L, "Italian GP", "Mugello", "Italy", "Scarperia",
                5.245, 15, 23, "1:45.187", "Francesco Bagnaia",
                2023, 1974
        );

        TrackResponse track2 = new TrackResponse(
                2L, "Spanish GP", "Jerez", "Spain", "Jerez de la Frontera",
                4.423, 13, 25, "1:36.170", "Marc Marquez",
                2023, 1987
        );

        when(trackService.getAllTracks()).thenReturn(List.of(track1, track2));

        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].circuitName").value("Mugello"))
                .andExpect(jsonPath("$[0].gpName").value("Italian GP"))
                .andExpect(jsonPath("$[1].circuitName").value("Jerez"))
                .andExpect(jsonPath("$[1].gpName").value("Spanish GP"));
    }

    @Test
    void getAllTracks_returnsEmptyList_whenNoTracksExists() throws Exception {
        when(trackService.getAllTracks())
                .thenReturn(List.of());

        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void getTrackById_returnsTrack() throws Exception {
        TrackResponse response = new TrackResponse(
                1L, "Italian GP", "Mugello", "Italy", "Scarperia",
                5.245, 15, 23, "1:45.187", "Francesco Bagnaia",
                2023, 1974
        );

        when(trackService.getTrackById(1L))
                .thenReturn(response);

        mockMvc.perform(get("/tracks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpName").value("Italian GP"))
                .andExpect(jsonPath("$.circuitName").value("Mugello"));
    }

    @Test
    void getTrackById_returns404WhenMissing() throws Exception {
        when(trackService.getTrackById(1L))
                .thenThrow(new NotFoundException("Track id 1 not found"));

        mockMvc.perform(get("/tracks/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Track id 1 not found"));
    }

    @Test
    void getTrackByCircuitName_returnsTrack() throws Exception {
        TrackResponse response = new TrackResponse(
                1L,
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

        when(trackService.getTrackByCircuitName("Circuito de Jerez – Ángel Nieto")).thenReturn(response);

        mockMvc.perform(get("/tracks/circuit")
                        .param("circuitName", "Circuito de Jerez – Ángel Nieto"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.circuitName").value("Circuito de Jerez – Ángel Nieto"));
    }

    @Test
    void getTracksByCountry_returnsTracks() throws Exception {
        TrackResponse response = new TrackResponse(
                1L,
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

        when(trackService.getTracksByCountry("Spain")).thenReturn(List.of(response));

        mockMvc.perform(get("/tracks/country")
                        .param("country", "Spain"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].country").value("Spain"));
    }

    @Test
    void getTracksSortedByLengthDesc_returnsTracks() throws Exception {
        TrackResponse response = new TrackResponse(
                1L,
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

        when(trackService.getTracksSortedByLengthDesc()).thenReturn(List.of(response));

        mockMvc.perform(get("/tracks/sorted/length-desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lengthKm").value(4.423));
    }

    @Test
    void createTrack_returnsCreatedTrack() throws Exception {
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

        TrackResponse response = new TrackResponse(
                1L,
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

        when(trackService.createTrack(any(TrackRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.circuitName").value("Mugello"))
                .andExpect(jsonPath("$.country").value("Italy"));
    }

    @Test
    void createTrack_returns400WhenRequestIsInvalid() throws Exception {
        TrackRequest request = new TrackRequest(
                "Italian GP",
                "",                 // invalid
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

        mockMvc.perform(post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTrack_returnsUpdatedTrack() throws Exception {
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

        TrackResponse response = new TrackResponse(
                1L,
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

        when(trackService.updateTrack(eq(1L), any(TrackRequest.class)))
                .thenReturn(response);

        mockMvc.perform(put("/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpName").value("Italian GP"))
                .andExpect(jsonPath("$.circuitName").value("Mugello"));
    }

    @Test
    void updateTrack_returns400WhenRequestIsInvalid() throws Exception {
        TrackRequest request = new TrackRequest(
                "Italian GP",
                "",                 // invalid
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

        mockMvc.perform(put("/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(trackService);
    }

    @Test
    void updateTrack_returns404WhenTrackMissing() throws Exception {
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

        when(trackService.updateTrack(eq(1L), any(TrackRequest.class)))
                .thenThrow(new NotFoundException("Track id 1 not found"));

        mockMvc.perform(put("/tracks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Track id 1 not found"));
    }

    @Test
    void deleteTrack_returns204() throws Exception {
        mockMvc.perform(delete("/tracks/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTrack_returns404WhenTrackMissing() throws Exception {
        doThrow(new NotFoundException("Track id 1 not found"))
                .when(trackService)
                .deleteTrack(1L);

        mockMvc.perform(delete("/tracks/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Track id 1 not found"));
    }
}
