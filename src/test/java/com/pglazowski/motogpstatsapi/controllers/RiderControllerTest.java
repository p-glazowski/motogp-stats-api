package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.RiderRequest;
import com.pglazowski.motogpstatsapi.dto.RiderResponse;
import com.pglazowski.motogpstatsapi.dto.TeamSummaryResponse;
import com.pglazowski.motogpstatsapi.services.RiderService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RiderControllerTest {

    @Mock
    private RiderService riderService;

    @InjectMocks
    private RiderController riderController;

    private RiderResponse riderResponse;
    private RiderRequest riderRequest;
    private TeamSummaryResponse teamSummary;
    private Long riderId;
    private Long teamId;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        riderId = 1L;
        teamId = 1L;

        teamSummary = new TeamSummaryResponse(
                teamId,
                "Ducati Lenovo Team",
                "Ducati",
                "Italy"
        );

        riderResponse = new RiderResponse(
                riderId,
                "Francesco",
                "Bagnaia",
                "Francesco Bagnaia",
                "Italy",
                29,
                63,
                teamSummary,
                25,
                45,
                18,
                2,
                "Two-time MotoGP World Champion.",
                "https://www.motogp.com/images/riders/bagnaia.jpg",
                LocalDateTime.now()
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
    void getRiders_ShouldReturnPageOfRiders_WhenNoFilters() {
        // Given
        Page<RiderResponse> page = new PageImpl<>(List.of(riderResponse));
        when(riderService.getRiders(null, null, null, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderController.getRiders(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(riderResponse);
        verify(riderService).getRiders(null, null, null, pageable);
    }

    @Test
    void getRiders_ShouldFilterByNationality() {
        // Given
        String nationality = "Italy";
        Page<RiderResponse> page = new PageImpl<>(List.of(riderResponse));
        when(riderService.getRiders(nationality, null, null, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderController.getRiders(nationality, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderService).getRiders(nationality, null, null, pageable);
    }

    @Test
    void getRiders_ShouldFilterByTeamId() {
        // Given
        Page<RiderResponse> page = new PageImpl<>(List.of(riderResponse));
        when(riderService.getRiders(null, teamId, null, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderController.getRiders(null, teamId, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderService).getRiders(null, teamId, null, pageable);
    }

    @Test
    void getRiders_ShouldSearchByName() {
        // Given
        String search = "Bagnaia";
        Page<RiderResponse> page = new PageImpl<>(List.of(riderResponse));
        when(riderService.getRiders(null, null, search, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderController.getRiders(null, null, search, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderService).getRiders(null, null, search, pageable);
    }

    @Test
    void getRiders_ShouldFilterByAllParameters() {
        // Given
        String nationality = "Italy";
        String search = "Bagnaia";
        Page<RiderResponse> page = new PageImpl<>(List.of(riderResponse));
        when(riderService.getRiders(nationality, teamId, search, pageable)).thenReturn(page);

        // When
        Page<RiderResponse> result = riderController.getRiders(nationality, teamId, search, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        verify(riderService).getRiders(nationality, teamId, search, pageable);
    }

    @Test
    void getRiderById_ShouldReturnRider_WhenExists() {
        // Given
        when(riderService.getRiderById(riderId)).thenReturn(riderResponse);

        // When
        RiderResponse result = riderController.getRiderById(riderId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(riderId);
        assertThat(result.fullName()).isEqualTo(riderResponse.fullName());
        verify(riderService).getRiderById(riderId);
    }

    @Test
    void getRiderByFullName_ShouldReturnRider_WhenExists() {
        // Given
        String fullName = "Francesco Bagnaia";
        when(riderService.getRiderByFullName(fullName)).thenReturn(riderResponse);

        // When
        RiderResponse result = riderController.getRiderByFullName(fullName);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.fullName()).isEqualTo(fullName);
        verify(riderService).getRiderByFullName(fullName);
    }

    @Test
    void getAllNationalities_ShouldReturnListOfNationalities() {
        // Given
        List<String> nationalities = List.of("Italy", "Spain", "France");
        when(riderService.getAllNationalities()).thenReturn(nationalities);

        // When
        List<String> result = riderController.getAllNationalities();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(3);
        assertThat(result).containsExactly("Italy", "Spain", "France");
        verify(riderService).getAllNationalities();
    }

    @Test
    void riderExists_ShouldReturnTrue_WhenRiderExists() {
        // Given
        String fullName = "Francesco Bagnaia";
        when(riderService.riderExists(fullName)).thenReturn(true);

        // When
        boolean result = riderController.riderExists(fullName);

        // Then
        assertThat(result).isTrue();
        verify(riderService).riderExists(fullName);
    }

    @Test
    void riderExists_ShouldReturnFalse_WhenRiderDoesNotExist() {
        // Given
        String fullName = "Non Existent Rider";
        when(riderService.riderExists(fullName)).thenReturn(false);

        // When
        boolean result = riderController.riderExists(fullName);

        // Then
        assertThat(result).isFalse();
        verify(riderService).riderExists(fullName);
    }

    @Test
    void createRider_ShouldCreateAndReturnRider() {
        // Given
        when(riderService.createRider(riderRequest)).thenReturn(riderResponse);

        // When
        RiderResponse result = riderController.createRider(riderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(riderId);
        assertThat(result.fullName()).isEqualTo(riderRequest.fullName());
        assertThat(result.firstName()).isEqualTo(riderRequest.firstName());
        assertThat(result.lastName()).isEqualTo(riderRequest.lastName());
        assertThat(result.nationality()).isEqualTo(riderRequest.nationality());
        assertThat(result.age()).isEqualTo(riderRequest.age());
        assertThat(result.raceNumber()).isEqualTo(riderRequest.raceNumber());
        assertThat(result.wins()).isEqualTo(riderRequest.wins());
        assertThat(result.podiums()).isEqualTo(riderRequest.podiums());
        assertThat(result.polePositions()).isEqualTo(riderRequest.polePositions());
        assertThat(result.worldTitles()).isEqualTo(riderRequest.worldTitles());
        assertThat(result.biography()).isEqualTo(riderRequest.biography());
        assertThat(result.imageUrl()).isEqualTo(riderRequest.imageUrl());
        verify(riderService).createRider(riderRequest);
    }

    @Test
    void updateRider_ShouldUpdateAndReturnRider() {
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

        RiderResponse updatedResponse = new RiderResponse(
                riderId,
                "Updated",
                "Name",
                "Updated Name",
                "Spain",
                30,
                99,
                teamSummary,
                30,
                50,
                20,
                3,
                "Updated biography.",
                "https://updated.com",
                LocalDateTime.now()
        );

        when(riderService.updateRider(riderId, updateRequest)).thenReturn(updatedResponse);

        // When
        RiderResponse result = riderController.updateRider(riderId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(riderId);
        assertThat(result.fullName()).isEqualTo("Updated Name");
        assertThat(result.nationality()).isEqualTo("Spain");
        assertThat(result.age()).isEqualTo(30);
        assertThat(result.raceNumber()).isEqualTo(99);
        assertThat(result.wins()).isEqualTo(30);
        assertThat(result.podiums()).isEqualTo(50);
        assertThat(result.polePositions()).isEqualTo(20);
        assertThat(result.worldTitles()).isEqualTo(3);
        verify(riderService).updateRider(riderId, updateRequest);
    }

    @Test
    void deleteRider_ShouldReturnNoContent_WhenRiderDeleted() {
        // Given
        doNothing().when(riderService).deleteRider(riderId);

        // When
        ResponseEntity<Void> response = riderController.deleteRider(riderId);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(riderService).deleteRider(riderId);
    }

    @Test
    void getRiders_ShouldReturnEmptyPage_WhenNoRiders() {
        // Given
        Page<RiderResponse> emptyPage = new PageImpl<>(List.of());
        when(riderService.getRiders(null, null, null, pageable)).thenReturn(emptyPage);

        // When
        Page<RiderResponse> result = riderController.getRiders(null, null, null, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(riderService).getRiders(null, null, null, pageable);
    }

    @Test
    void getRiders_ShouldHandleEmptySearchResults() {
        // Given
        String search = "NonExistent";
        Page<RiderResponse> emptyPage = new PageImpl<>(List.of());
        when(riderService.getRiders(null, null, search, pageable)).thenReturn(emptyPage);

        // When
        Page<RiderResponse> result = riderController.getRiders(null, null, search, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
        verify(riderService).getRiders(null, null, search, pageable);
    }

    @Test
    void getAllNationalities_ShouldReturnEmptyList_WhenNoRiders() {
        // Given
        when(riderService.getAllNationalities()).thenReturn(List.of());

        // When
        List<String> result = riderController.getAllNationalities();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
        verify(riderService).getAllNationalities();
    }

    @Test
    void createRider_ShouldHandleAllFieldsCorrectly() {
        // Given
        when(riderService.createRider(riderRequest)).thenReturn(riderResponse);

        // When
        RiderResponse result = riderController.createRider(riderRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.team()).isNotNull();
        assertThat(result.team().id()).isEqualTo(teamId);
        assertThat(result.team().name()).isEqualTo("Ducati Lenovo Team");
        assertThat(result.team().manufacturer()).isEqualTo("Ducati");
        assertThat(result.team().country()).isEqualTo("Italy");
        assertThat(result.createdAt()).isNotNull();
        verify(riderService).createRider(riderRequest);
    }

    @Test
    void updateRider_ShouldHandleAllFieldsCorrectly() {
        // Given
        RiderRequest updateRequest = new RiderRequest(
                "Test",
                "Rider",
                "Test Rider",
                "Testland",
                25,
                77,
                teamId,
                10,
                20,
                5,
                1,
                "Test biography.",
                "https://test.com"
        );

        TeamSummaryResponse testTeamSummary = new TeamSummaryResponse(
                teamId,
                "Test Team",
                "Test",
                "Testland"
        );

        RiderResponse updatedResponse = new RiderResponse(
                riderId,
                "Test",
                "Rider",
                "Test Rider",
                "Testland",
                25,
                77,
                testTeamSummary,
                10,
                20,
                5,
                1,
                "Test biography.",
                "https://test.com",
                LocalDateTime.now()
        );

        when(riderService.updateRider(riderId, updateRequest)).thenReturn(updatedResponse);

        // When
        RiderResponse result = riderController.updateRider(riderId, updateRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.firstName()).isEqualTo("Test");
        assertThat(result.lastName()).isEqualTo("Rider");
        assertThat(result.fullName()).isEqualTo("Test Rider");
        assertThat(result.nationality()).isEqualTo("Testland");
        assertThat(result.age()).isEqualTo(25);
        assertThat(result.raceNumber()).isEqualTo(77);
        assertThat(result.wins()).isEqualTo(10);
        assertThat(result.podiums()).isEqualTo(20);
        assertThat(result.polePositions()).isEqualTo(5);
        assertThat(result.worldTitles()).isEqualTo(1);
        assertThat(result.biography()).isEqualTo("Test biography.");
        assertThat(result.imageUrl()).isEqualTo("https://test.com");
        assertThat(result.team().name()).isEqualTo("Test Team");
        verify(riderService).updateRider(riderId, updateRequest);
    }
}