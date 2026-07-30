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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RiderService {

    private final RiderRepository riderRepository;
    private final TeamRepository teamRepository;

    public RiderService(RiderRepository riderRepository, TeamRepository teamRepository) {
        this.riderRepository = riderRepository;
        this.teamRepository = teamRepository;
    }

    public RiderResponse toDto(Rider rider) {
        TeamSummaryResponse teamSummary = null;
        if (rider.getTeam() != null) {
            teamSummary = new TeamSummaryResponse(
                    rider.getTeam().getId(),
                    rider.getTeam().getName(),
                    rider.getTeam().getManufacturer(),
                    rider.getTeam().getCountry()
            );
        }

        return new RiderResponse(
                rider.getId(),
                rider.getFirstName(),
                rider.getLastName(),
                rider.getFullName(),
                rider.getNationality(),
                rider.getAge(),
                rider.getRaceNumber(),
                teamSummary,
                rider.getWins(),
                rider.getPodiums(),
                rider.getPolePositions(),
                rider.getWorldTitles(),
                rider.getBiography(),
                rider.getImageUrl(),
                rider.getCreatedAt()
        );
    }

    public Rider toEntity(RiderRequest request, Team team) {
        return new Rider(
                request.firstName(),
                request.lastName(),
                request.fullName(),
                request.nationality(),
                request.age(),
                request.raceNumber(),
                team,
                request.wins(),
                request.podiums(),
                request.polePositions(),
                request.worldTitles(),
                request.biography(),
                request.imageUrl()
        );
    }

    public Page<RiderResponse> getRiders(String nationality, Long teamId, String search, Pageable pageable) {
        Page<Rider> riders;

        boolean hasNationality = nationality != null && !nationality.isEmpty();
        boolean hasTeamId = teamId != null;
        boolean hasSearch = search != null && !search.isEmpty();

        if (hasNationality && hasTeamId && hasSearch) {
            // All three filters combined
            riders = riderRepository.findByNationalityIgnoreCaseAndTeamIdAndFullNameContainingIgnoreCase(
                    nationality, teamId, search, pageable);
        } else if (hasNationality && hasTeamId) {
            // Nationality + Team
            riders = riderRepository.findByNationalityIgnoreCaseAndTeamId(nationality, teamId, pageable);
        } else if (hasNationality && hasSearch) {
            // Nationality + Search
            riders = riderRepository.findByNationalityIgnoreCaseAndFullNameContainingIgnoreCase(
                    nationality, search, pageable);
        } else if (hasTeamId && hasSearch) {
            // Team + Search
            riders = riderRepository.findByTeamIdAndFullNameContainingIgnoreCase(teamId, search, pageable);
        } else if (hasNationality) {
            riders = riderRepository.findByNationalityIgnoreCase(nationality, pageable);
        } else if (hasTeamId) {
            riders = riderRepository.findByTeamId(teamId, pageable);
        } else if (hasSearch) {
            riders = riderRepository.findByFullNameContainingIgnoreCase(search, pageable);
        } else {
            riders = riderRepository.findAll(pageable);
        }

        return riders.map(this::toDto);
    }

    public RiderResponse getRiderById(Long id) {
        Rider rider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider with id " + id + " not found"));
        return toDto(rider);
    }

    public RiderResponse getRiderByFullName(String fullName) {
        Rider rider = riderRepository.findByFullNameIgnoreCase(fullName)
                .orElseThrow(() -> new NotFoundException("Rider with name '" + fullName + "' not found"));
        return toDto(rider);
    }

    @Transactional
    public RiderResponse createRider(RiderRequest request) {
        // Check if rider with this full name already exists
        if (riderRepository.existsByFullNameIgnoreCase(request.fullName())) {
            throw new DuplicateResourceException("Rider with name '" + request.fullName() + "' already exists");
        }

        // Check if race number is already taken
        if (riderRepository.existsByRaceNumber(request.raceNumber())) {
            throw new DuplicateResourceException("Race number " + request.raceNumber() + " is already taken");
        }

        // Find the team
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new NotFoundException("Team with id " + request.teamId() + " not found"));

        // Create and save rider
        Rider rider = toEntity(request, team);
        Rider savedRider = riderRepository.save(rider);
        return toDto(savedRider);
    }

    @Transactional
    public RiderResponse updateRider(Long id, RiderRequest request) {
        // Find existing rider
        Rider existingRider = riderRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Rider with id " + id + " not found"));

        // Check if full name conflicts with another rider
        if (!existingRider.getFullName().equalsIgnoreCase(request.fullName()) &&
                riderRepository.existsByFullNameIgnoreCase(request.fullName())) {
            throw new DuplicateResourceException("Rider with name '" + request.fullName() + "' already exists");
        }

        // Check if race number conflicts with another rider
        if (!existingRider.getRaceNumber().equals(request.raceNumber()) &&
                riderRepository.existsByRaceNumber(request.raceNumber())) {
            throw new DuplicateResourceException("Race number " + request.raceNumber() + " is already taken");
        }

        // Find the team
        Team team = teamRepository.findById(request.teamId())
                .orElseThrow(() -> new NotFoundException("Team with id " + request.teamId() + " not found"));

        // Update rider fields
        existingRider.setFirstName(request.firstName());
        existingRider.setLastName(request.lastName());
        existingRider.setFullName(request.fullName());
        existingRider.setNationality(request.nationality());
        existingRider.setAge(request.age());
        existingRider.setRaceNumber(request.raceNumber());
        existingRider.setTeam(team);
        existingRider.setWins(request.wins());
        existingRider.setPodiums(request.podiums());
        existingRider.setPolePositions(request.polePositions());
        existingRider.setWorldTitles(request.worldTitles());
        existingRider.setBiography(request.biography());
        existingRider.setImageUrl(request.imageUrl());

        Rider updatedRider = riderRepository.save(existingRider);
        return toDto(updatedRider);
    }

    @Transactional
    public void deleteRider(Long id) {
        if (!riderRepository.existsById(id)) {
            throw new NotFoundException("Rider with id " + id + " not found");
        }
        riderRepository.deleteById(id);
    }

    public List<String> getAllNationalities() {
        return riderRepository.findAllDistinctNationalities();
    }

    public boolean riderExists(String fullName) {
        return riderRepository.existsByFullNameIgnoreCase(fullName);
    }
}
