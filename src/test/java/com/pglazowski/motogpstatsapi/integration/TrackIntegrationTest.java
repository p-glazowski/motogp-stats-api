package com.pglazowski.motogpstatsapi.integration;

import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TrackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TrackIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TrackRepository trackRepository;

    @BeforeEach
    void cleanDatabase() {
        trackRepository.deleteAll();
    }

    @Test
    void createTrack_createsTrackAndReturnsResponse() throws Exception {

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


        mockMvc.perform(post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.circuitName")
                        .value("Mugello"))
                .andExpect(jsonPath("$.country")
                        .value("Italy"));


        mockMvc.perform(get("/tracks/circuit")
                        .param("circuitName", "Mugello"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpName")
                        .value("Italian GP"));
    }


    @Test
    void createTrack_returns400_whenValidationFails() throws Exception {

        TrackRequest invalidRequest = new TrackRequest(
                "",
                "",
                "",
                "",
                -1.0,
                0,
                0,
                null,
                null,
                null,
                1974
        );


        mockMvc.perform(post("/tracks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }


    @Test
    void getAllTracks_returnsSavedTracks() throws Exception {

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


        mockMvc.perform(get("/tracks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].circuitName",
                        is("Jerez")));
    }


    @Test
    void getTrackById_returnsTrack() throws Exception {

        Track saved = trackRepository.save(new Track(
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


        mockMvc.perform(get("/tracks/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.circuitName")
                        .value("Mugello"));
    }


    @Test
    void getTrackById_returns404_whenTrackDoesNotExist() throws Exception {

        mockMvc.perform(get("/tracks/99999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status")
                        .value(404))
                .andExpect(jsonPath("$.message")
                        .value("Track id 99999 not found"));
    }


    @Test
    void getTrackByCircuitName_returnsTrack() throws Exception {

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


        mockMvc.perform(get("/tracks/circuit")
                        .param("circuitName", "Misano"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.country")
                        .value("Italy"));
    }


    @Test
    void getTracksByCountry_returnsTracks() throws Exception {

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


        mockMvc.perform(get("/tracks/country")
                        .param("country", "Italy"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }


    @Test
    void getTracksSortedByLengthDesc_returnsSortedTracks() throws Exception {

        trackRepository.save(new Track(
                "Long GP",
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
                "Short GP",
                "Short Circuit",
                "Country",
                "City",
                4.0,
                10,
                20,
                null,
                null,
                null,
                2001
        ));


        mockMvc.perform(get("/tracks/sorted/length-desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].lengthKm")
                        .value(6.0))
                .andExpect(jsonPath("$[1].lengthKm")
                        .value(4.0));
    }


    @Test
    void updateTrack_updatesExistingTrack() throws Exception {

        Track saved = trackRepository.save(new Track(
                "Old GP",
                "Old Circuit",
                "Italy",
                "Old City",
                4.0,
                10,
                20,
                null,
                null,
                null,
                2000
        ));


        TrackRequest updateRequest = new TrackRequest(
                "New GP",
                "New Circuit",
                "Spain",
                "Madrid",
                5.5,
                15,
                25,
                null,
                null,
                null,
                2025
        );


        mockMvc.perform(put("/tracks/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.gpName")
                        .value("New GP"))
                .andExpect(jsonPath("$.country")
                        .value("Spain"));
    }


    @Test
    void updateTrack_returns404_whenTrackMissing() throws Exception {

        TrackRequest request = new TrackRequest(
                "GP",
                "Circuit",
                "Country",
                "City",
                5.0,
                10,
                20,
                null,
                null,
                null,
                2000
        );


        mockMvc.perform(put("/tracks/99999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteTrack_deletesExistingTrack() throws Exception {

        Track saved = trackRepository.save(new Track(
                "Delete GP",
                "Delete Circuit",
                "Italy",
                "Rome",
                5.0,
                10,
                20,
                null,
                null,
                null,
                2000
        ));


        mockMvc.perform(delete("/tracks/{id}", saved.getId()))
                .andExpect(status().isNoContent());


        mockMvc.perform(get("/tracks/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteTrack_returns404_whenTrackMissing() throws Exception {

        mockMvc.perform(delete("/tracks/99999"))
                .andExpect(status().isNotFound());
    }


}
