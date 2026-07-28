package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.dto.TeamResponse;
import com.pglazowski.motogpstatsapi.exceptions.DuplicateResourceException;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceTest {

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private TeamService teamService;

    private Team team;
    private TeamRequest teamRequest;
    private Long teamId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        team = new Team(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
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
    void toDto_ShouldConvertTeamToTeamResponse() {
        // When
        TeamResponse response = teamService.toDto(team);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(team.getId());
        assertThat(response.name()).isEqualTo(team.getName());
        assertThat(response.manufacturer()).isEqualTo(team.getManufacturer());
        assertThat(response.country()).isEqualTo(team.getCountry());
        assertThat(response.teamPrincipal()).isEqualTo(team.getTeamPrincipal());
        assertThat(response.baseLocation()).isEqualTo(team.getBaseLocation());
        assertThat(response.foundedYear()).isEqualTo(team.getFoundedYear());
        assertThat(response.website()).isEqualTo(team.getWebsite());
    }

    @Test
    void toEntity_ShouldConvertTeamRequestToTeam() {
        // When
        Team result = teamService.toEntity(teamRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(teamRequest.name());
        assertThat(result.getManufacturer()).isEqualTo(teamRequest.manufacturer());
        assertThat(result.getCountry()).isEqualTo(teamRequest.country());
        assertThat(result.getTeamPrincipal()).isEqualTo(teamRequest.teamPrincipal());
        assertThat(result.getBaseLocation()).isEqualTo(teamRequest.baseLocation());
        assertThat(result.getFoundedYear()).isEqualTo(teamRequest.foundedYear());
        assertThat(result.getWebsite()).isEqualTo(teamRequest.website());
    }

    @Test
    void getTeams_ShouldReturnAllTeams_WhenNoFilters() {
        // Given
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.getTeams(null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent().get(0).name()).isEqualTo(team.getName());
        verify(teamRepository).findAll(pageable);
        verify(teamRepository, never()).findByCountryIgnoreCase(anyString(), any());
        verify(teamRepository, never()).findByManufacturerIgnoreCase(anyString(), any());
        verify(teamRepository, never()).findByCountryIgnoreCaseAndManufacturerIgnoreCase(anyString(), anyString(), any());
    }

    @Test
    void getTeams_ShouldReturnTeamsFilteredByCountry_WhenCountryProvided() {
        // Given
        String country = "Italy";
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findByCountryIgnoreCase(country, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.getTeams(country, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamRepository).findByCountryIgnoreCase(country, pageable);
    }

    @Test
    void getTeams_ShouldReturnTeamsFilteredByManufacturer_WhenManufacturerProvided() {
        // Given
        String manufacturer = "Ducati";
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findByManufacturerIgnoreCase(manufacturer, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.getTeams(null, manufacturer, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamRepository).findByManufacturerIgnoreCase(manufacturer, pageable);
    }

    @Test
    void getTeams_ShouldReturnTeamsFilteredByCountryAndManufacturer_WhenBothProvided() {
        // Given
        String country = "Italy";
        String manufacturer = "Ducati";
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findByCountryIgnoreCaseAndManufacturerIgnoreCase(country, manufacturer, pageable))
                .thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.getTeams(country, manufacturer, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(teamRepository).findByCountryIgnoreCaseAndManufacturerIgnoreCase(country, manufacturer, pageable);
    }

    @Test
    void getTeamById_ShouldReturnTeam_WhenTeamExists() {
        // Given
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));

        // When
        TeamResponse result = teamService.getTeamById(teamId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(teamId);
        assertThat(result.name()).isEqualTo(team.getName());
        verify(teamRepository).findById(teamId);
    }

    @Test
    void getTeamById_ShouldThrowNotFoundException_WhenTeamDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(teamRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> teamService.getTeamById(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team with id " + nonExistentId + " not found");
        verify(teamRepository).findById(nonExistentId);
    }

    @Test
    void createTeam_ShouldCreateAndReturnTeam_WhenNameDoesNotExist() {
        // Given
        when(teamRepository.existsByNameIgnoreCase(teamRequest.name())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(team);

        // When
        TeamResponse result = teamService.createTeam(teamRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(teamRequest.name());
        verify(teamRepository).existsByNameIgnoreCase(teamRequest.name());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void createTeam_ShouldThrowDuplicateResourceException_WhenNameAlreadyExists() {
        // Given
        when(teamRepository.existsByNameIgnoreCase(teamRequest.name())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> teamService.createTeam(teamRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Team with name '" + teamRequest.name() + "' already exists");
        verify(teamRepository).existsByNameIgnoreCase(teamRequest.name());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void updateTeam_ShouldUpdateAndReturnTeam_WhenTeamExistsAndNameDoesNotConflict() {
        // Given
        TeamRequest updatedRequest = new TeamRequest(
                "Updated Team Name",
                "Yamaha",
                "Japan",
                "New Principal",
                "New Location",
                2000,
                "https://updated.com"
        );
        Team existingTeam = new Team(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.existsByNameIgnoreCase(updatedRequest.name())).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(existingTeam);

        // When
        TeamResponse result = teamService.updateTeam(teamId, updatedRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(updatedRequest.name());
        assertThat(result.manufacturer()).isEqualTo(updatedRequest.manufacturer());
        assertThat(result.country()).isEqualTo(updatedRequest.country());
        verify(teamRepository).findById(teamId);
        verify(teamRepository).existsByNameIgnoreCase(updatedRequest.name());
        verify(teamRepository).save(any(Team.class));
    }

    @Test
    void updateTeam_ShouldThrowNotFoundException_WhenTeamDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(teamRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> teamService.updateTeam(nonExistentId, teamRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team with id " + nonExistentId + " not found");
        verify(teamRepository).findById(nonExistentId);
        verify(teamRepository, never()).existsByNameIgnoreCase(anyString());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void updateTeam_ShouldThrowDuplicateResourceException_WhenNewNameConflicts() {
        // Given
        String existingName = "Honda HRC Castrol";
        TeamRequest updatedRequest = new TeamRequest(
                existingName,
                "Honda",
                "Japan",
                "Alberto Puig",
                "Tokyo, Japan",
                1995,
                "https://www.honda.racing"
        );

        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(teamRepository.existsByNameIgnoreCase(updatedRequest.name())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> teamService.updateTeam(teamId, updatedRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Team with name '" + updatedRequest.name() + "' already exists");
        verify(teamRepository).findById(teamId);
        verify(teamRepository).existsByNameIgnoreCase(updatedRequest.name());
        verify(teamRepository, never()).save(any(Team.class));
    }

    @Test
    void deleteTeam_ShouldDeleteTeam_WhenTeamExists() {
        // Given
        when(teamRepository.existsById(teamId)).thenReturn(true);
        doNothing().when(teamRepository).deleteById(teamId);

        // When
        teamService.deleteTeam(teamId);

        // Then
        verify(teamRepository).existsById(teamId);
        verify(teamRepository).deleteById(teamId);
    }

    @Test
    void deleteTeam_ShouldThrowNotFoundException_WhenTeamDoesNotExist() {
        // Given
        Long nonExistentId = 999L;
        when(teamRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> teamService.deleteTeam(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team with id " + nonExistentId + " not found");
        verify(teamRepository).existsById(nonExistentId);
        verify(teamRepository, never()).deleteById(anyLong());
    }

    @Test
    void searchTeams_ShouldReturnTeamsMatchingQuery() {
        // Given
        String query = "Ducati";
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findByNameContainingIgnoreCase(query, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.searchTeams(query, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).name()).isEqualTo(team.getName());
        verify(teamRepository).findByNameContainingIgnoreCase(query, pageable);
    }

    @Test
    void getTeamByName_ShouldReturnTeam_WhenTeamExists() {
        // Given
        String name = "Ducati Lenovo Team";
        when(teamRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(team));

        // When
        TeamResponse result = teamService.getTeamByName(name);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.name()).isEqualTo(name);
        verify(teamRepository).findByNameIgnoreCase(name);
    }

    @Test
    void getTeamByName_ShouldThrowNotFoundException_WhenTeamDoesNotExist() {
        // Given
        String name = "Non Existent Team";
        when(teamRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> teamService.getTeamByName(name))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team with name '" + name + "' not found");
        verify(teamRepository).findByNameIgnoreCase(name);
    }

    @Test
    void getAllCountries_ShouldReturnListOfCountries() {
        // Given
        List<String> countries = List.of("Italy", "Japan", "Austria");
        when(teamRepository.findAllDistinctCountries()).thenReturn(countries);

        // When
        List<String> result = teamService.getAllCountries();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Italy", "Japan", "Austria");
        verify(teamRepository).findAllDistinctCountries();
    }

    @Test
    void getAllManufacturers_ShouldReturnListOfManufacturers() {
        // Given
        List<String> manufacturers = List.of("Ducati", "Yamaha", "Honda", "KTM");
        when(teamRepository.findAllDistinctManufacturers()).thenReturn(manufacturers);

        // When
        List<String> result = teamService.getAllManufacturers();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(4);
        assertThat(result).containsExactly("Ducati", "Yamaha", "Honda", "KTM");
        verify(teamRepository).findAllDistinctManufacturers();
    }

    @Test
    void teamExists_ShouldReturnTrue_WhenTeamExists() {
        // Given
        String name = "Ducati Lenovo Team";
        when(teamRepository.existsByNameIgnoreCase(name)).thenReturn(true);

        // When
        boolean result = teamService.teamExists(name);

        // Then
        assertThat(result).isTrue();
        verify(teamRepository).existsByNameIgnoreCase(name);
    }

    @Test
    void teamExists_ShouldReturnFalse_WhenTeamDoesNotExist() {
        // Given
        String name = "Non Existent Team";
        when(teamRepository.existsByNameIgnoreCase(name)).thenReturn(false);

        // When
        boolean result = teamService.teamExists(name);

        // Then
        assertThat(result).isFalse();
        verify(teamRepository).existsByNameIgnoreCase(name);
    }

    @Test
    void getTeamsFoundedBetween_ShouldReturnTeamsInYearRange() {
        // Given
        Integer startYear = 1995;
        Integer endYear = 2000;
        List<Team> teams = List.of(team);
        Page<Team> page = new PageImpl<>(teams);
        when(teamRepository.findByFoundedYearBetween(startYear, endYear, pageable)).thenReturn(page);

        // When
        Page<TeamResponse> result = teamService.getTeamsFoundedBetween(startYear, endYear, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).foundedYear()).isEqualTo(team.getFoundedYear());
        verify(teamRepository).findByFoundedYearBetween(startYear, endYear, pageable);
    }
}
