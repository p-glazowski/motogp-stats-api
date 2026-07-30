package com.pglazowski.motogpstatsapi.integration;

import com.pglazowski.motogpstatsapi.dto.RiderRequest;
import com.pglazowski.motogpstatsapi.models.Rider;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.repositories.RiderRepository;
import com.pglazowski.motogpstatsapi.repositories.TeamRepository;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class RiderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RiderRepository riderRepository;

    @Autowired
    private TeamRepository teamRepository;

    private Team ducatiTeam;
    private Team yamahaTeam;
    private Rider rider1;
    private Rider rider2;
    private Rider rider3;
    private RiderRequest riderRequest;

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

        riderRepository.saveAll(List.of(rider1, rider2, rider3));

        riderRequest = new RiderRequest(
                "Alex",
                "Rins",
                "Alex Rins",
                "Spain",
                30,
                42,
                yamahaTeam.getId(),
                5,
                12,
                4,
                0,
                "Known for smooth riding style.",
                "https://www.motogp.com/images/riders/rins.jpg"
        );
    }

    @Test
    void getRiders_ShouldReturnAllRiders_WhenNoFilters() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                .andExpect(jsonPath("$.content[*].fullName").value(hasItems(
                        "Francesco Bagnaia",
                        "Marc Marquez",
                        "Fabio Quartararo"
                )))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }

    @Test
    void getRiders_ShouldFilterByNationality() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("nationality", "Spain")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].fullName").value("Marc Marquez"))
                .andExpect(jsonPath("$.content[0].nationality").value("Spain"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getRiders_ShouldFilterByTeamId() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("teamId", ducatiTeam.getId().toString())
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(2)))
                .andExpect(jsonPath("$.content[*].fullName").value(hasItems(
                        "Francesco Bagnaia",
                        "Marc Marquez"
                )))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    void getRiders_ShouldSearchByName() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("search", "Marquez")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].fullName").value("Marc Marquez"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getRiders_ShouldReturnEmptyPage_WhenNoMatches() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("nationality", "Germany")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void getRiderById_ShouldReturnRider_WhenExists() throws Exception {
        Long riderId = rider1.getId();

        mockMvc.perform(get("/riders/{id}", riderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(riderId))
                .andExpect(jsonPath("$.fullName").value("Francesco Bagnaia"))
                .andExpect(jsonPath("$.firstName").value("Francesco"))
                .andExpect(jsonPath("$.lastName").value("Bagnaia"))
                .andExpect(jsonPath("$.nationality").value("Italy"))
                .andExpect(jsonPath("$.age").value(29))
                .andExpect(jsonPath("$.raceNumber").value(63))
                .andExpect(jsonPath("$.wins").value(25))
                .andExpect(jsonPath("$.podiums").value(45))
                .andExpect(jsonPath("$.polePositions").value(18))
                .andExpect(jsonPath("$.worldTitles").value(2))
                .andExpect(jsonPath("$.biography").value("Two-time MotoGP World Champion."))
                .andExpect(jsonPath("$.imageUrl").value("https://www.motogp.com/images/riders/bagnaia.jpg"))
                .andExpect(jsonPath("$.team.id").value(ducatiTeam.getId()))
                .andExpect(jsonPath("$.team.name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.team.manufacturer").value("Ducati"))
                .andExpect(jsonPath("$.team.country").value("Italy"));
    }

    @Test
    void getRiderById_ShouldReturn404_WhenRiderDoesNotExist() throws Exception {
        mockMvc.perform(get("/riders/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rider with id 999 not found"));
    }

    @Test
    void getRiderByFullName_ShouldReturnRider_WhenExists() throws Exception {
        mockMvc.perform(get("/riders/name")
                        .param("fullName", "Francesco Bagnaia"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Francesco Bagnaia"))
                .andExpect(jsonPath("$.nationality").value("Italy"))
                .andExpect(jsonPath("$.team.name").value("Ducati Lenovo Team"));
    }

    @Test
    void getRiderByFullName_ShouldReturnRider_CaseInsensitive() throws Exception {
        mockMvc.perform(get("/riders/name")
                        .param("fullName", "marc marquez"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Marc Marquez"))
                .andExpect(jsonPath("$.nationality").value("Spain"));
    }

    @Test
    void getRiderByFullName_ShouldReturn404_WhenRiderDoesNotExist() throws Exception {
        mockMvc.perform(get("/riders/name")
                        .param("fullName", "Non Existent Rider"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rider with name 'Non Existent Rider' not found"));
    }

    @Test
    void getAllNationalities_ShouldReturnDistinctNationalities() throws Exception {
        mockMvc.perform(get("/riders/nationalities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(3)))
                .andExpect(jsonPath("$").value(hasItems("Italy", "Spain", "France")))
                .andExpect(jsonPath("$[0]").value("France"))
                .andExpect(jsonPath("$[1]").value("Italy"))
                .andExpect(jsonPath("$[2]").value("Spain"));
    }

    @Test
    void riderExists_ShouldReturnTrue_WhenRiderExists() throws Exception {
        mockMvc.perform(get("/riders/exists")
                        .param("fullName", "Francesco Bagnaia"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void riderExists_ShouldReturnFalse_WhenRiderDoesNotExist() throws Exception {
        mockMvc.perform(get("/riders/exists")
                        .param("fullName", "Non Existent Rider"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void createRider_ShouldCreateAndReturnRider() throws Exception {
        mockMvc.perform(post("/riders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(riderRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("Alex Rins"))
                .andExpect(jsonPath("$.firstName").value("Alex"))
                .andExpect(jsonPath("$.lastName").value("Rins"))
                .andExpect(jsonPath("$.nationality").value("Spain"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.raceNumber").value(42))
                .andExpect(jsonPath("$.wins").value(5))
                .andExpect(jsonPath("$.podiums").value(12))
                .andExpect(jsonPath("$.polePositions").value(4))
                .andExpect(jsonPath("$.worldTitles").value(0))
                .andExpect(jsonPath("$.biography").value("Known for smooth riding style."))
                .andExpect(jsonPath("$.imageUrl").value("https://www.motogp.com/images/riders/rins.jpg"))
                .andExpect(jsonPath("$.team.id").value(yamahaTeam.getId()))
                .andExpect(jsonPath("$.team.name").value("Monster Energy Yamaha MotoGP"))
                .andExpect(jsonPath("$.id").exists());

        // Verify it was actually saved
        List<Rider> allRiders = riderRepository.findAll();
        assertThat(allRiders).hasSize(4);
        assertThat(allRiders).extracting(Rider::getFullName).contains("Alex Rins");
    }

    @Test
    void createRider_ShouldReturn409_WhenFullNameAlreadyExists() throws Exception {
        RiderRequest duplicateRequest = new RiderRequest(
                "Francesco",
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                99,
                ducatiTeam.getId(),
                25,
                45,
                18,
                2,
                "Duplicate rider.",
                "https://www.duplicate.com"
        );

        mockMvc.perform(post("/riders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Rider with name 'Francesco Bagnaia' already exists"));

        // Verify no new rider was created
        assertThat(riderRepository.count()).isEqualTo(3);
    }

    @Test
    void createRider_ShouldReturn409_WhenRaceNumberAlreadyExists() throws Exception {
        RiderRequest duplicateRequest = new RiderRequest(
                "Test",
                "Rider",
                "Test Rider",
                "Test",
                25,
                63,
                ducatiTeam.getId(),
                0,
                0,
                0,
                0,
                "Test rider.",
                "https://www.test.com"
        );

        mockMvc.perform(post("/riders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Race number 63 is already taken"));

        // Verify no new rider was created
        assertThat(riderRepository.count()).isEqualTo(3);
    }

    @Test
    void createRider_ShouldReturn404_WhenTeamNotFound() throws Exception {
        RiderRequest invalidRequest = new RiderRequest(
                "Test",
                "Rider",
                "Test Rider",
                "Test",
                25,
                99,
                999L,
                0,
                0,
                0,
                0,
                "Test rider.",
                "https://www.test.com"
        );

        mockMvc.perform(post("/riders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team with id 999 not found"));

        // Verify no new rider was created
        assertThat(riderRepository.count()).isEqualTo(3);
    }

    @Test
    void createRider_ShouldReturn400_WhenInvalidData() throws Exception {
        RiderRequest invalidRequest = new RiderRequest(
                "", // Empty first name
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                63,
                ducatiTeam.getId(),
                25,
                45,
                18,
                2,
                "Two-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/bagnaia.jpg"
        );

        mockMvc.perform(post("/riders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateRider_ShouldUpdateAndReturnRider() throws Exception {
        Long riderId = rider1.getId();

        RiderRequest updateRequest = new RiderRequest(
                "Updated",
                "Name",
                "Updated Name",
                "Spain",
                30,
                99,
                yamahaTeam.getId(),
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com"
        );

        mockMvc.perform(put("/riders/{id}", riderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(riderId))
                .andExpect(jsonPath("$.fullName").value("Updated Name"))
                .andExpect(jsonPath("$.firstName").value("Updated"))
                .andExpect(jsonPath("$.lastName").value("Name"))
                .andExpect(jsonPath("$.nationality").value("Spain"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.raceNumber").value(99))
                .andExpect(jsonPath("$.wins").value(30))
                .andExpect(jsonPath("$.podiums").value(50))
                .andExpect(jsonPath("$.polePositions").value(20))
                .andExpect(jsonPath("$.worldTitles").value(3))
                .andExpect(jsonPath("$.biography").value("Updated biography."))
                .andExpect(jsonPath("$.imageUrl").value("https://updated.com"))
                .andExpect(jsonPath("$.team.id").value(yamahaTeam.getId()))
                .andExpect(jsonPath("$.team.name").value("Monster Energy Yamaha MotoGP"));

        // Verify it was actually updated
        Rider updatedRider = riderRepository.findById(riderId).orElseThrow();
        assertThat(updatedRider.getFullName()).isEqualTo("Updated Name");
        assertThat(updatedRider.getTeam().getId()).isEqualTo(yamahaTeam.getId());
    }

    @Test
    void updateRider_ShouldReturn404_WhenRiderDoesNotExist() throws Exception {
        mockMvc.perform(put("/riders/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(riderRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rider with id 999 not found"));
    }

    @Test
    void updateRider_ShouldReturn409_WhenFullNameConflicts() throws Exception {
        Long riderId = rider1.getId();

        RiderRequest conflictRequest = new RiderRequest(
                "Marc",
                "Marquez",
                "Marc Marquez",
                "Spain",
                33,
                99,
                ducatiTeam.getId(),
                64,
                101,
                64,
                6,
                "Six-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/marquez.jpg"
        );

        mockMvc.perform(put("/riders/{id}", riderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Rider with name 'Marc Marquez' already exists"));

        // Verify rider was not updated
        Rider unchangedRider = riderRepository.findById(riderId).orElseThrow();
        assertThat(unchangedRider.getFullName()).isEqualTo("Francesco Bagnaia");
    }

    @Test
    void updateRider_ShouldReturn409_WhenRaceNumberConflicts() throws Exception {
        Long riderId = rider1.getId();

        RiderRequest conflictRequest = new RiderRequest(
                "Updated",
                "Name",
                "Updated Name",
                "Spain",
                30,
                93,
                ducatiTeam.getId(),
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com"
        );

        mockMvc.perform(put("/riders/{id}", riderId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Race number 93 is already taken"));

        // Verify rider was not updated
        Rider unchangedRider = riderRepository.findById(riderId).orElseThrow();
        assertThat(unchangedRider.getRaceNumber()).isEqualTo(63);
    }

    @Test
    void deleteRider_ShouldDeleteRiderAndReturnNoContent() throws Exception {
        Long riderId = rider2.getId();

        mockMvc.perform(delete("/riders/{id}", riderId))
                .andExpect(status().isNoContent());

        // Verify it was actually deleted
        assertThat(riderRepository.findById(riderId)).isEmpty();
        assertThat(riderRepository.count()).isEqualTo(2);
    }

    @Test
    void deleteRider_ShouldReturn404_WhenRiderDoesNotExist() throws Exception {
        mockMvc.perform(delete("/riders/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Rider with id 999 not found"));

        // Verify no riders were deleted
        assertThat(riderRepository.count()).isEqualTo(3);
    }

    @Test
    void getRiders_ShouldSupportPagination() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("page", "0")
                        .param("size", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(2)))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(2))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.first").value(true))
                .andExpect(jsonPath("$.last").value(false));
    }

    @Test
    void getRiders_ShouldSupportSorting() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("sort", "fullName,desc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].fullName").value("Marc Marquez"))
                .andExpect(jsonPath("$.content[1].fullName").value("Francesco Bagnaia"))
                .andExpect(jsonPath("$.content[2].fullName").value("Fabio Quartararo"));
    }

    @Test
    void getRiders_ShouldFilterByTeamAndSearch() throws Exception {
        mockMvc.perform(get("/riders")
                        .param("teamId", ducatiTeam.getId().toString())
                        .param("search", "Marquez")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].fullName").value("Marc Marquez"))
                .andExpect(jsonPath("$.content[0].team.id").value(ducatiTeam.getId()));
    }
}