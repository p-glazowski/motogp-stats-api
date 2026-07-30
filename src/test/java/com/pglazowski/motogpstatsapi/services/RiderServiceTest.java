package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.RiderRequest;
import com.pglazowski.motogpstatsapi.dto.RiderResponse;
import com.pglazowski.motogpstatsapi.dto.TeamSummaryResponse;
import com.pglazowski.motogpstatsapi.exceptions.DuplicateResourceException;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.models.Rider;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.repositories.RiderRepository;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiderServiceTest {

    @Mock
    private RiderRepository riderRepository;

    @Mock
    private TeamRepository teamRepository;

    @InjectMocks
    private RiderService riderService;

    private Team team;
    private Rider rider;
    private RiderRequest riderRequest;
    private Long riderId;
    private Long teamId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        riderId = 1L;
        teamId = 1L;

        team = new Team(
                "Ducati Lenovo Team",
                "Ducati",
                "Italy",
                "Luigi Dall'Igna",
                "Bologna, Italy",
                1999,
                "https://www.ducati.com/racing"
        );

        rider = new Rider(
                "Francesco",
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                63,
                team,
                25,
                45,
                18,
                2,
                "Two-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/bagnaia.jpg"
        );

        riderRequest = new RiderRequest(
                "Francesco",
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                63,
                teamId,
                25,
                45,
                18,
                2,
                "Two-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/bagnaia.jpg"
        );

        pageable = Pageable.ofSize(10);
    }

    @Test
    void toDto_ShouldConvertRiderToRiderResponse() {
        // When
        RiderResponse response = riderService.toDto(rider);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(rider.getId());
        assertThat(response.firstName()).isEqualTo(rider.getFirstName());
        assertThat(response.lastName()).isEqualTo(rider.getLastName());
        assertThat(response.fullName()).isEqualTo(rider.getFullName());
        assertThat(response.nationality()).isEqualTo(rider.getNationality());
        assertThat(response.age()).isEqualTo(rider.getAge());
        assertThat(response.raceNumber()).isEqualTo(rider.getRaceNumber());
        assertThat(response.wins()).isEqualTo(rider.getWins());
        assertThat(response.podiums()).isEqualTo(rider.getPodiums());
        assertThat(response.polePositions()).isEqualTo(rider.getPolePositions());
        assertThat(response.worldTitles()).isEqualTo(rider.getWorldTitles());
        assertThat(response.biography()).isEqualTo(rider.getBiography());
        assertThat(response.imageUrl()).isEqualTo(rider.getImageUrl());

        // Verify team summary
        assertThat(response.team()).isNotNull();
        assertThat(response.team().id()).isEqualTo(team.getId());
        assertThat(response.team().name()).isEqualTo(team.getName());
        assertThat(response.team().manufacturer()).isEqualTo(team.getManufacturer());
        assertThat(response.team().country()).isEqualTo(team.getCountry());
    }

    @Test
    void toDto_ShouldHandleNullTeam() {
        // Given
        Rider riderWithoutTeam = new Rider(
                "Test",
                "Rider",
                "Test Rider",
                "Test",
                30,
                99,
                null,
                0,
                0,
                0,
                0,
                null,
                null
        );

        // When
        RiderResponse response = riderService.toDto(riderWithoutTeam);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.team()).isNull();
    }

    @Test
    void toEntity_ShouldConvertRequestToRider() {
        // When
        Rider result = riderService.toEntity(riderRequest, team);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo(riderRequest.firstName());
        assertThat(result.getLastName()).isEqualTo(riderRequest.lastName());
        assertThat(result.getFullName()).isEqualTo(riderRequest.fullName());
        assertThat(result.getNationality()).isEqualTo(riderRequest.nationality());
        assertThat(result.getAge()).isEqualTo(riderRequest.age());
        assertThat(result.getRaceNumber()).isEqualTo(riderRequest.raceNumber());
        assertThat(result.getTeam()).isEqualTo(team);
        assertThat(result.getWins()).isEqualTo(riderRequest.wins());
        assertThat(result.getPodiums()).isEqualTo(riderRequest.podiums());
        assertThat(result.getPolePositions()).isEqualTo(riderRequest.polePositions());
        assertThat(result.getWorldTitles()).isEqualTo(riderRequest.worldTitles());
        assertThat(result.getBiography()).isEqualTo(riderRequest.biography());
        assertThat(result.getImageUrl()).isEqualTo(riderRequest.imageUrl());
    }

    @Test
    void getRiders_ShouldReturnAllRiders_WhenNoFilters() {
        // Given
        List<Rider> riders = List.of(rider);
        Page<Rider> page = new PageImpl<>(riders);
        when(riderRepository.findAll(pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderService.getRiders(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).fullName()).isEqualTo(rider.getFullName());
        verify(riderRepository).findAll(pageable);
        verify(riderRepository, never()).findByNationalityIgnoreCase(anyString(), any());
        verify(riderRepository, never()).findByTeamId(anyLong(), any());
        verify(riderRepository, never()).findByFullNameContainingIgnoreCase(anyString(), any());
    }

    @Test
    void getRiders_ShouldFilterByNationality() {
        // Given
        String nationality = "Italy";
        List<Rider> riders = List.of(rider);
        Page<Rider> page = new PageImpl<>(riders);
        when(riderRepository.findByNationalityIgnoreCase(nationality, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderService.getRiders(nationality, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderRepository).findByNationalityIgnoreCase(nationality, pageable);
    }

    @Test
    void getRiders_ShouldFilterByTeamId() {
        // Given
        List<Rider> riders = List.of(rider);
        Page<Rider> page = new PageImpl<>(riders);
        when(riderRepository.findByTeamId(teamId, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderService.getRiders(null, teamId, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderRepository).findByTeamId(teamId, pageable);
    }

    @Test
    void getRiders_ShouldSearchByName() {
        // Given
        String search = "Bagnaia";
        List<Rider> riders = List.of(rider);
        Page<Rider> page = new PageImpl<>(riders);
        when(riderRepository.findByFullNameContainingIgnoreCase(search, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderService.getRiders(null, null, search, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderRepository).findByFullNameContainingIgnoreCase(search, pageable);
    }

    @Test
    void getRiderById_ShouldReturnRider_WhenExists() {
        // Given
        when(riderRepository.findById(rider.getId())).thenReturn(Optional.of(rider));

        // When
        RiderResponse result = riderService.getRiderById(rider.getId());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(rider.getId());
        assertThat(result.fullName()).isEqualTo(rider.getFullName());
        verify(riderRepository).findById(rider.getId());
    }

    @Test
    void getRiderById_ShouldThrowNotFoundException_WhenNotExists() {
        // Given
        Long nonExistentId = 999L;
        when(riderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> riderService.getRiderById(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rider with id " + nonExistentId + " not found");
        verify(riderRepository).findById(nonExistentId);
    }

    @Test
    void getRiderByFullName_ShouldReturnRider_WhenExists() {
        // Given
        String fullName = "Francesco Bagnaia";
        when(riderRepository.findByFullNameIgnoreCase(fullName)).thenReturn(Optional.of(rider));

        // When
        RiderResponse result = riderService.getRiderByFullName(fullName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(fullName);
        verify(riderRepository).findByFullNameIgnoreCase(fullName);
    }

    @Test
    void getRiderByFullName_ShouldThrowNotFoundException_WhenNotExists() {
        // Given
        String fullName = "Non Existent Rider";
        when(riderRepository.findByFullNameIgnoreCase(fullName)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> riderService.getRiderByFullName(fullName))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rider with name '" + fullName + "' not found");
        verify(riderRepository).findByFullNameIgnoreCase(fullName);
    }

    @Test
    void createRider_ShouldCreateAndReturnRider_WhenValid() {
        // Given
        when(riderRepository.existsByFullNameIgnoreCase(riderRequest.fullName())).thenReturn(false);
        when(riderRepository.existsByRaceNumber(riderRequest.raceNumber())).thenReturn(false);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);

        // When
        RiderResponse result = riderService.createRider(riderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(riderRequest.fullName());
        verify(riderRepository).existsByFullNameIgnoreCase(riderRequest.fullName());
        verify(riderRepository).existsByRaceNumber(riderRequest.raceNumber());
        verify(teamRepository).findById(teamId);
        verify(riderRepository).save(any(Rider.class));
    }

    @Test
    void createRider_ShouldThrowDuplicateResourceException_WhenFullNameExists() {
        // Given
        when(riderRepository.existsByFullNameIgnoreCase(riderRequest.fullName())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> riderService.createRider(riderRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Rider with name '" + riderRequest.fullName() + "' already exists");
        verify(riderRepository).existsByFullNameIgnoreCase(riderRequest.fullName());
        verify(riderRepository, never()).existsByRaceNumber(anyInt());
        verify(teamRepository, never()).findById(anyLong());
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void createRider_ShouldThrowDuplicateResourceException_WhenRaceNumberExists() {
        // Given
        when(riderRepository.existsByFullNameIgnoreCase(riderRequest.fullName())).thenReturn(false);
        when(riderRepository.existsByRaceNumber(riderRequest.raceNumber())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> riderService.createRider(riderRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Race number " + riderRequest.raceNumber() + " is already taken");
        verify(riderRepository).existsByFullNameIgnoreCase(riderRequest.fullName());
        verify(riderRepository).existsByRaceNumber(riderRequest.raceNumber());
        verify(teamRepository, never()).findById(anyLong());
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void createRider_ShouldThrowNotFoundException_WhenTeamNotFound() {
        // Given
        when(riderRepository.existsByFullNameIgnoreCase(riderRequest.fullName())).thenReturn(false);
        when(riderRepository.existsByRaceNumber(riderRequest.raceNumber())).thenReturn(false);
        when(teamRepository.findById(teamId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> riderService.createRider(riderRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Team with id " + teamId + " not found");
        verify(riderRepository).existsByFullNameIgnoreCase(riderRequest.fullName());
        verify(riderRepository).existsByRaceNumber(riderRequest.raceNumber());
        verify(teamRepository).findById(teamId);
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void updateRider_ShouldUpdateAndReturnRider_WhenValid() {
        // Given
        RiderRequest updateRequest = new RiderRequest(
                "Updated",
                "Name",
                "Updated Name",
                "Spain",
                30,
                99,
                teamId,
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com"
        );

        when(riderRepository.findById(riderId)).thenReturn(Optional.of(rider));
        when(riderRepository.existsByFullNameIgnoreCase(updateRequest.fullName())).thenReturn(false);
        when(riderRepository.existsByRaceNumber(updateRequest.raceNumber())).thenReturn(false);
        when(teamRepository.findById(teamId)).thenReturn(Optional.of(team));
        when(riderRepository.save(any(Rider.class))).thenReturn(rider);

        // When
        RiderResponse result = riderService.updateRider(riderId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(riderRepository).findById(riderId);
        verify(riderRepository).existsByFullNameIgnoreCase(updateRequest.fullName());
        verify(riderRepository).existsByRaceNumber(updateRequest.raceNumber());
        verify(teamRepository).findById(teamId);
        verify(riderRepository).save(any(Rider.class));
    }

    @Test
    void updateRider_ShouldThrowNotFoundException_WhenRiderNotFound() {
        // Given
        Long nonExistentId = 999L;
        when(riderRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> riderService.updateRider(nonExistentId, riderRequest))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rider with id " + nonExistentId + " not found");
        verify(riderRepository).findById(nonExistentId);
        verify(riderRepository, never()).existsByFullNameIgnoreCase(anyString());
        verify(riderRepository, never()).existsByRaceNumber(anyInt());
        verify(teamRepository, never()).findById(anyLong());
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void updateRider_ShouldThrowDuplicateResourceException_WhenFullNameConflicts() {
        // Given
        RiderRequest updateRequest = new RiderRequest(
                "Existing",
                "Rider",
                "Existing Rider",
                "Spain",
                30,
                99,
                teamId,
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com"
        );

        when(riderRepository.findById(riderId)).thenReturn(Optional.of(rider));
        when(riderRepository.existsByFullNameIgnoreCase(updateRequest.fullName())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> riderService.updateRider(riderId, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Rider with name '" + updateRequest.fullName() + "' already exists");
        verify(riderRepository).findById(riderId);
        verify(riderRepository).existsByFullNameIgnoreCase(updateRequest.fullName());
        verify(riderRepository, never()).existsByRaceNumber(anyInt());
        verify(teamRepository, never()).findById(anyLong());
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void updateRider_ShouldThrowDuplicateResourceException_WhenRaceNumberConflicts() {
        // Given
        RiderRequest updateRequest = new RiderRequest(
                "Updated",
                "Name",
                "Updated Name",
                "Spain",
                30,
                99,
                teamId,
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com"
        );

        when(riderRepository.findById(riderId)).thenReturn(Optional.of(rider));
        when(riderRepository.existsByFullNameIgnoreCase(updateRequest.fullName())).thenReturn(false);
        when(riderRepository.existsByRaceNumber(updateRequest.raceNumber())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> riderService.updateRider(riderId, updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessageContaining("Race number " + updateRequest.raceNumber() + " is already taken");
        verify(riderRepository).findById(riderId);
        verify(riderRepository).existsByFullNameIgnoreCase(updateRequest.fullName());
        verify(riderRepository).existsByRaceNumber(updateRequest.raceNumber());
        verify(teamRepository, never()).findById(anyLong());
        verify(riderRepository, never()).save(any(Rider.class));
    }

    @Test
    void deleteRider_ShouldDeleteRider_WhenExists() {
        // Given
        when(riderRepository.existsById(riderId)).thenReturn(true);
        doNothing().when(riderRepository).deleteById(riderId);

        // When
        riderService.deleteRider(riderId);

        // Then
        verify(riderRepository).existsById(riderId);
        verify(riderRepository).deleteById(riderId);
    }

    @Test
    void deleteRider_ShouldThrowNotFoundException_WhenNotExists() {
        // Given
        Long nonExistentId = 999L;
        when(riderRepository.existsById(nonExistentId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> riderService.deleteRider(nonExistentId))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("Rider with id " + nonExistentId + " not found");
        verify(riderRepository).existsById(nonExistentId);
        verify(riderRepository, never()).deleteById(anyLong());
    }

    @Test
    void getAllNationalities_ShouldReturnListOfNationalities() {
        // Given
        List<String> nationalities = List.of("Italy", "Spain", "France");
        when(riderRepository.findAllDistinctNationalities()).thenReturn(nationalities);

        // When
        List<String> result = riderService.getAllNationalities();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Italy", "Spain", "France");
        verify(riderRepository).findAllDistinctNationalities();
    }

    @Test
    void riderExists_ShouldReturnTrue_WhenRiderExists() {
        // Given
        String fullName = "Francesco Bagnaia";
        when(riderRepository.existsByFullNameIgnoreCase(fullName)).thenReturn(true);

        // When
        boolean result = riderService.riderExists(fullName);

        // Then
        assertThat(result).isTrue();
        verify(riderRepository).existsByFullNameIgnoreCase(fullName);
    }

    @Test
    void riderExists_ShouldReturnFalse_WhenRiderDoesNotExist() {
        // Given
        String fullName = "Non Existent Rider";
        when(riderRepository.existsByFullNameIgnoreCase(fullName)).thenReturn(false);

        // When
        boolean result = riderService.riderExists(fullName);

        // Then
        assertThat(result).isFalse();
        verify(riderRepository).existsByFullNameIgnoreCase(fullName);
    }
}