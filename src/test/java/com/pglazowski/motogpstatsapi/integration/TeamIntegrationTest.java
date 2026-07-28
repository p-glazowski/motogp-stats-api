package com.pglazowski.motogpstatsapi.integration;

import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class TeamIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TeamRepository teamRepository;

    private Team ducatiTeam;
    private Team yamahaTeam;
    private Team hondaTeam;
    private TeamRequest teamRequest;

    @BeforeEach
    void setUp() {
        teamRepository.deleteAll();

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

        hondaTeam = new Team(
                "Honda HRC Castrol",
                "Honda",
                "Japan",
                "Alberto Puig",
                "Tokyo, Japan",
                1995,
                "https://www.honda.racing"
        );

        teamRepository.saveAll(List.of(ducatiTeam, yamahaTeam, hondaTeam));

        teamRequest = new TeamRequest(
                "Aprilia Racing",
                "Aprilia",
                "Italy",
                "Massimo Rivola",
                "Noale, Italy",
                2015,
                "https://www.aprilia.com/racing"
        );
    }

    @Test
    void getTeams_ShouldReturnAllTeams_WhenNoFilters() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                .andExpect(jsonPath("$.content[*].name").value(hasItems(
                        "Ducati Lenovo Team",
                        "Monster Energy Yamaha MotoGP",
                        "Honda HRC Castrol"
                )))
                .andExpect(jsonPath("$.totalElements").value(3))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.pageable.pageNumber").value(0))
                .andExpect(jsonPath("$.pageable.pageSize").value(10));
    }

    @Test
    void getTeams_ShouldFilterByCountry() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("country", "Italy")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.content[0].country").value("Italy"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTeams_ShouldFilterByManufacturer() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("manufacturer", "Yamaha")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Monster Energy Yamaha MotoGP"))
                .andExpect(jsonPath("$.content[0].manufacturer").value("Yamaha"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTeams_ShouldFilterByCountryAndManufacturer() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("country", "Italy")
                        .param("manufacturer", "Ducati")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.content[0].country").value("Italy"))
                .andExpect(jsonPath("$.content[0].manufacturer").value("Ducati"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void getTeams_ShouldReturnEmptyPage_WhenNoTeamsMatch() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("country", "Spain")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0))
                .andExpect(jsonPath("$.totalPages").value(0));
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() throws Exception {
        Long teamId = ducatiTeam.getId();

        mockMvc.perform(get("/teams/{id}", teamId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teamId))
                .andExpect(jsonPath("$.name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.manufacturer").value("Ducati"))
                .andExpect(jsonPath("$.country").value("Italy"))
                .andExpect(jsonPath("$.teamPrincipal").value("Luigi Dall'Igna"))
                .andExpect(jsonPath("$.baseLocation").value("Bologna, Italy"))
                .andExpect(jsonPath("$.foundedYear").value(1999))
                .andExpect(jsonPath("$.website").value("https://www.ducati.com/racing"));
    }

    @Test
    void getTeamById_ShouldReturn404_WhenTeamDoesNotExist() throws Exception {
        mockMvc.perform(get("/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team with id 999 not found"));
    }

    @Test
    void getTeamByName_ShouldReturnTeam_WhenTeamExists() throws Exception {
        mockMvc.perform(get("/teams/name")
                        .param("name", "Ducati Lenovo Team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.manufacturer").value("Ducati"))
                .andExpect(jsonPath("$.country").value("Italy"));
    }

    @Test
    void getTeamByName_ShouldReturnTeam_CaseInsensitive() throws Exception {
        mockMvc.perform(get("/teams/name")
                        .param("name", "ducati lenovo team"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Ducati Lenovo Team"));
    }

    @Test
    void getTeamByName_ShouldReturn404_WhenTeamDoesNotExist() throws Exception {
        mockMvc.perform(get("/teams/name")
                        .param("name", "Non Existent Team"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team with name 'Non Existent Team' not found"));
    }

    @Test
    void searchTeams_ShouldReturnMatchingTeams() throws Exception {
        mockMvc.perform(get("/teams/search")
                        .param("query", "team")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(1)))
                .andExpect(jsonPath("$.content[0].name").value("Ducati Lenovo Team"))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    void searchTeams_ShouldReturnEmptyResults_WhenNoMatch() throws Exception {
        mockMvc.perform(get("/teams/search")
                        .param("query", "xyz")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getTeamsFoundedBetween_ShouldReturnTeamsInYearRange() throws Exception {
        mockMvc.perform(get("/teams/founded-between")
                        .param("startYear", "1995")
                        .param("endYear", "2000")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(3)))
                .andExpect(jsonPath("$.content[*].name").value(hasItems(
                        "Ducati Lenovo Team",
                        "Monster Energy Yamaha MotoGP",
                        "Honda HRC Castrol"
                )))
                .andExpect(jsonPath("$.totalElements").value(3));
    }

    @Test
    void getTeamsFoundedBetween_ShouldReturnEmpty_WhenNoTeamsInRange() throws Exception {
        mockMvc.perform(get("/teams/founded-between")
                        .param("startYear", "2025")
                        .param("endYear", "2030")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").value(hasSize(0)))
                .andExpect(jsonPath("$.totalElements").value(0));
    }

    @Test
    void getAllCountries_ShouldReturnDistinctCountries() throws Exception {
        mockMvc.perform(get("/teams/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(2)))
                .andExpect(jsonPath("$").value(hasItems("Italy", "Japan")));
    }

    @Test
    void getAllManufacturers_ShouldReturnDistinctManufacturers() throws Exception {
        mockMvc.perform(get("/teams/manufacturers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").value(hasSize(3)))
                .andExpect(jsonPath("$").value(hasItems("Ducati", "Yamaha", "Honda")));
    }

    @Test
    void teamExists_ShouldReturnTrue_WhenTeamExists() throws Exception {
        mockMvc.perform(get("/teams/exists")
                        .param("name", "Ducati Lenovo Team"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void teamExists_ShouldReturnFalse_WhenTeamDoesNotExist() throws Exception {
        mockMvc.perform(get("/teams/exists")
                        .param("name", "Non Existent Team"))
                .andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void createTeam_ShouldCreateAndReturnTeam() throws Exception {
        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Aprilia Racing"))
                .andExpect(jsonPath("$.manufacturer").value("Aprilia"))
                .andExpect(jsonPath("$.country").value("Italy"))
                .andExpect(jsonPath("$.teamPrincipal").value("Massimo Rivola"))
                .andExpect(jsonPath("$.baseLocation").value("Noale, Italy"))
                .andExpect(jsonPath("$.foundedYear").value(2015))
                .andExpect(jsonPath("$.website").value("https://www.aprilia.com/racing"));

        // Verify it was actually saved
        List<Team> allTeams = teamRepository.findAll();
        assertThat(allTeams).hasSize(4);
        assertThat(allTeams).extracting(Team::getName).contains("Aprilia Racing");
    }

    @Test
    void createTeam_ShouldReturn409_WhenTeamNameAlreadyExists() throws Exception {
        TeamRequest duplicateRequest = new TeamRequest(
                "Ducati Lenovo Team", // This already exists
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Team with name 'Ducati Lenovo Team' already exists"));

        // Verify no new team was created
        assertThat(teamRepository.count()).isEqualTo(3);
    }

    @Test
    void createTeam_ShouldReturn400_WhenInvalidData() throws Exception {
        TeamRequest invalidRequest = new TeamRequest(
                "", // Empty name
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        mockMvc.perform(post("/teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateTeam_ShouldUpdateAndReturnTeam() throws Exception {
        Long teamId = ducatiTeam.getId();

        TeamRequest updateRequest = new TeamRequest(
                "Updated Ducati Team",
                "Ducati",
                "Italy",
                "Updated Principal",
                "Updated Location",
                2000,
                "https://updated.ducati.com"
        );

        mockMvc.perform(put("/teams/{id}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teamId))
                .andExpect(jsonPath("$.name").value("Updated Ducati Team"))
                .andExpect(jsonPath("$.teamPrincipal").value("Updated Principal"))
                .andExpect(jsonPath("$.baseLocation").value("Updated Location"))
                .andExpect(jsonPath("$.foundedYear").value(2000))
                .andExpect(jsonPath("$.website").value("https://updated.ducati.com"));

        // Verify it was actually updated
        Team updatedTeam = teamRepository.findById(teamId).orElseThrow();
        assertThat(updatedTeam.getName()).isEqualTo("Updated Ducati Team");
        assertThat(updatedTeam.getTeamPrincipal()).isEqualTo("Updated Principal");
    }

    @Test
    void updateTeam_ShouldReturn404_WhenTeamDoesNotExist() throws Exception {
        mockMvc.perform(put("/teams/{id}", 999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamRequest)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team with id 999 not found"));
    }

    @Test
    void updateTeam_ShouldReturn409_WhenNameAlreadyExists() throws Exception {
        Long teamId = ducatiTeam.getId();

        TeamRequest conflictRequest = new TeamRequest(
                "Honda HRC Castrol", // This name already exists (hondaTeam)
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        mockMvc.perform(put("/teams/{id}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conflictRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Team with name 'Honda HRC Castrol' already exists"));

        // Verify team was not updated
        Team unchangedTeam = teamRepository.findById(teamId).orElseThrow();
        assertThat(unchangedTeam.getName()).isEqualTo("Ducati Lenovo Team");
    }

    @Test
    void deleteTeam_ShouldDeleteTeamAndReturnNoContent() throws Exception {
        Long teamId = ducatiTeam.getId();

        mockMvc.perform(delete("/teams/{id}", teamId))
                .andExpect(status().isNoContent());

        // Verify it was actually deleted
        assertThat(teamRepository.findById(teamId)).isEmpty();
        assertThat(teamRepository.count()).isEqualTo(2);
    }

    @Test
    void deleteTeam_ShouldReturn404_WhenTeamDoesNotExist() throws Exception {
        mockMvc.perform(delete("/teams/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Team with id 999 not found"));

        // Verify no teams were deleted
        assertThat(teamRepository.count()).isEqualTo(3);
    }

    @Test
    void getTeams_ShouldSupportPagination() throws Exception {
        mockMvc.perform(get("/teams")
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
    void getTeams_ShouldSupportSorting() throws Exception {
        mockMvc.perform(get("/teams")
                        .param("sort", "name,desc")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Monster Energy Yamaha MotoGP"))
                .andExpect(jsonPath("$.content[1].name").value("Honda HRC Castrol"))
                .andExpect(jsonPath("$.content[2].name").value("Ducati Lenovo Team"));
    }
}
