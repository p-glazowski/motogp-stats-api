package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.RiderSummaryResponse;
import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.dto.TeamResponse;
import com.pglazowski.motogpstatsapi.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamControllerTest {

    @Mock
    private TeamService teamService;

    @InjectMocks
    private TeamController teamController;

    private TeamResponse teamResponse;
    private TeamRequest teamRequest;
    private Long teamId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {

        List<RiderSummaryResponse> riders = List.of(
                new RiderSummaryResponse(1L, "Francesco Bagnaia", 63, "Italy"),
                new RiderSummaryResponse(2L, "Marc Marquez", 93, "Spain")
        );

        teamResponse = new TeamResponse(
                teamId,
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing",
                riders
        );

        teamRequest = new TeamRequest(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        pageable = Pageable.ofSize(10);
    }

    @Test
    void getTeams_ShouldReturnPageOfTeams_WhenNoFilters() {
        // Given
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.getTeams(null, null, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.getTeams(null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(teamResponse);
        verify(teamService).getTeams(null, null, pageable);
    }

    @Test
    void getTeams_ShouldReturnPageOfTeams_WhenCountryFilterProvided() {
        // Given
        String country = "Italy";
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.getTeams(country, null, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.getTeams(country, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamService).getTeams(country, null, pageable);
    }

    @Test
    void getTeams_ShouldReturnPageOfTeams_WhenManufacturerFilterProvided() {
        // Given
        String manufacturer = "Ducati";
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.getTeams(null, manufacturer, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.getTeams(null, manufacturer, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamService).getTeams(null, manufacturer, pageable);
    }

    @Test
    void getTeams_ShouldReturnPageOfTeams_WhenBothFiltersProvided() {
        // Given
        String country = "Italy";
        String manufacturer = "Ducati";
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.getTeams(country, manufacturer, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.getTeams(country, manufacturer, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamService).getTeams(country, manufacturer, pageable);
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() {
        // Given
        when(teamService.getTeamById(teamId)).thenReturn(teamResponse);

        // When
        TeamResponse result = teamController.getTeamById(teamId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(teamId);
        assertThat(result.name()).isEqualTo(teamResponse.name());
        verify(teamService).getTeamById(teamId);
    }

    @Test
    void getTeamByName_ShouldReturnTeam_WhenTeamExists() {
        // Given
        String name = "Ducati Lenovo Team";
        when(teamService.getTeamByName(name)).thenReturn(teamResponse);

        // When
        TeamResponse result = teamController.getTeamByName(name);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(name);
        verify(teamService).getTeamByName(name);
    }

    @Test
    void searchTeams_ShouldReturnPageOfTeams_WhenQueryProvided() {
        // Given
        String query = "Ducati";
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.searchTeams(query, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.searchTeams(query, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).contains("Ducati");
        verify(teamService).searchTeams(query, pageable);
    }

    @Test
    void getTeamsFoundedBetween_ShouldReturnPageOfTeams() {
        // Given
        Integer startYear = 1995;
        Integer endYear = 2000;
        Page<TeamResponse> page = new PageImpl<>(List.of(teamResponse));
        when(teamService.getTeamsFoundedBetween(startYear, endYear, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamController.getTeamsFoundedBetween(startYear, endYear, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamService).getTeamsFoundedBetween(startYear, endYear, pageable);
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountries() {
        // Given
        List<String> countries = List.of("Italy", "Japan", "Austria");
        when(teamService.getAllCountries()).thenReturn(countries);

        // When
        List<String> result = teamController.getAllCountries();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Italy", "Japan", "Austria");
        verify(teamService).getAllCountries();
    }

    @Test
    void getAllManufacturers_ShouldReturnListOfManufacturers() {
        // Given
        List<String> manufacturers = List.of("Ducati", "Yamaha", "Honda");
        when(teamService.getAllManufacturers()).thenReturn(manufacturers);

        // When
        List<String> result = teamController.getAllManufacturers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Ducati", "Yamaha", "Honda");
        verify(teamService).getAllManufacturers();
    }

    @Test
    void teamExists_ShouldReturnTrue_WhenTeamExists() {
        // Given
        String name = "Ducati Lenovo Team";
        when(teamService.teamExists(name)).thenReturn(true);

        // When
        boolean result = teamController.teamExists(name);

        // Then
        assertThat(result).isTrue();
        verify(teamService).teamExists(name);
    }

    @Test
    void teamExists_ShouldReturnFalse_WhenTeamDoesNotExist() {
        // Given
        String name = "Non Existent Team";
        when(teamService.teamExists(name)).thenReturn(false);

        // When
        boolean result = teamController.teamExists(name);

        // Then
        assertThat(result).isFalse();
        verify(teamService).teamExists(name);
    }

    @Test
    void createTeam_ShouldReturnCreatedTeam() {
        // Given
        when(teamService.createTeam(teamRequest)).thenReturn(teamResponse);

        // When
        TeamResponse result = teamController.createTeam(teamRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(teamId);
        assertThat(result.name()).isEqualTo(teamRequest.name());
        assertThat(result.manufacturer()).isEqualTo(teamRequest.manufacturer());
        verify(teamService).createTeam(teamRequest);
    }

    @Test
    void updateTeam_ShouldReturnUpdatedTeam() {
        // Given
        TeamRequest updatedRequest = new TeamRequest(
                "Updated Team",
                "Yamaha",
                "Japan",
                "New Principal",
                "New Location",
                2000,
                "https://updated.com"
        );
        TeamResponse updatedResponse = new TeamResponse(
                teamId,
                "Updated Team",
                "Yamaha",
                "Japan",
                "New Principal",
                "New Location",
                2000,
                "https://updated.com",
                List.of()
        );
        when(teamService.updateTeam(teamId, updatedRequest)).thenReturn(updatedResponse);

        // When
        TeamResponse result = teamController.updateTeam(teamId, updatedRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(teamId);
        assertThat(result.name()).isEqualTo("Updated Team");
        assertThat(result.manufacturer()).isEqualTo("Yamaha");
        verify(teamService).updateTeam(teamId, updatedRequest);
    }

    @Test
    void deleteTeam_ShouldReturnNoContent_WhenTeamDeleted() {
        // Given
        doNothing().when(teamService).deleteTeam(teamId);

        // When
        ResponseEntity<Void> response = teamController.deleteTeam(teamId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(teamService).deleteTeam(teamId);
    }

    @Test
    void getTeamById_ShouldReturnTeamWithCorrectData() {
        // Given
        when(teamService.getTeamById(teamId)).thenReturn(teamResponse);

        // When
        TeamResponse result = teamController.getTeamById(teamId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo("Ducati Lenovo Team");
        assertThat(result.manufacturer()).isEqualTo("Ducati");
        assertThat(result.country()).isEqualTo("Italy");
        assertThat(result.teamPrincipal()).isEqualTo("Luigi Dall'Igna");
        assertThat(result.baseLocation()).isEqualTo("Bologna, Italy");
        assertThat(result.foundedYear()).isEqualTo(1999);
        assertThat(result.website()).isEqualTo("https://www.ducati.com/racing");
        verify(teamService).getTeamById(teamId);
    }

    @Test
    void getTeams_ShouldHandleEmptyPage() {
        // Given
        Page<TeamResponse> emptyPage = new PageImpl<>(List.of());
        when(teamService.getTeams(null, null, pageable)).thenReturn(emptyPage);

        // When
        Page<TeamResponse> result = teamController.getTeams(null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(teamService).getTeams(null, null, pageable);
    }

    @Test
    void searchTeams_ShouldHandleEmptyResults() {
        // Given
        String query = "NonExistent";
        Page<TeamResponse> emptyPage = new PageImpl<>(List.of());
        when(teamService.searchTeams(query, pageable)).thenReturn(emptyPage);

        // When
        Page<TeamResponse> result = teamController.searchTeams(query, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(teamService).searchTeams(query, pageable);
    }

    @Test
    void getTeamsFoundedBetween_ShouldHandleEmptyResults() {
        // Given
        Integer startYear = 2025;
        Integer endYear = 2030;
        Page<TeamResponse> emptyPage = new PageImpl<>(List.of());
        when(teamService.getTeamsFoundedBetween(startYear, endYear, pageable)).thenReturn(emptyPage);

        // When
        Page<TeamResponse> result = teamController.getTeamsFoundedBetween(startYear, endYear, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(teamService).getTeamsFoundedBetween(startYear, endYear, pageable);
    }
}
