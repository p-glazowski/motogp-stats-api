package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.dto.TeamResponse;
import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.exceptions.DuplicateResourceException;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public TeamResponse toDto(Team team) {
        return new TeamResponse(
                team.getId(),
                team.getName(),
                team.getManufacturer(),
                team.getCountry(),
                team.getTeamPrincipal(),
                team.getBaseLocation(),
                team.getFoundedYear(),
                team.getWebsite()
        );
    }

    public Team toEntity(TeamRequest request) {
        return new Team(
                request.name(),
                request.manufacturer(),
                request.country(),
                request.teamPrincipal(),
                request.baseLocation(),
                request.foundedYear(),
                request.website()
        );
    }

    public Page<TeamResponse> getTeams(String country,
                                            String manufacturer,
                                            Pageable pageable) {

        Page<Team> teams;

        if (country != null && manufacturer != null) {
            teams = teamRepository.findByCountryIgnoreCaseAndManufacturerIgnoreCase(country, manufacturer, pageable);

        } else if (country != null) {
            teams = teamRepository.findByCountryIgnoreCase(country, pageable);

        } else if (manufacturer != null) {
            teams = teamRepository.findByManufacturerIgnoreCase(manufacturer, pageable);

        } else {
            teams = teamRepository.findAll(pageable);
        }

        return teams.map(this::toDto);
    }

    public TeamResponse getTeamById(Long id) {
        Team foundTeam = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team with id " + id + " not found"));

        return toDto(foundTeam);
    }

    public TeamResponse createTeam(TeamRequest request) {
        if (teamRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Team with name '" + request.name() + "' already exists");
        }
        Team team = toEntity(request);
        Team savedTeam = teamRepository.save(team);
        return toDto(savedTeam);
    }

    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team existingTeam = teamRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Team with id " + id + " not found"));

        if (!existingTeam.getName().equalsIgnoreCase(request.name()) &&
                teamRepository.existsByNameIgnoreCase(request.name())) {
            throw new DuplicateResourceException("Team with name '" + request.name() + "' already exists");
        }

        existingTeam.setName(request.name());
        existingTeam.setManufacturer(request.manufacturer());
        existingTeam.setCountry(request.country());
        existingTeam.setTeamPrincipal(request.teamPrincipal());
        existingTeam.setBaseLocation(request.baseLocation());
        existingTeam.setFoundedYear(request.foundedYear());
        existingTeam.setWebsite(request.website());

        Team updatedTeam = teamRepository.save(existingTeam);
        return toDto(updatedTeam);
    }

    public void deleteTeam(Long id) {
        if (!teamRepository.existsById(id)) {
            throw new NotFoundException("Team with id " + id + " not found");
        }
        teamRepository.deleteById(id);
    }

    public Page<TeamResponse> searchTeams(String query, Pageable pageable) {
        return teamRepository.findByNameContainingIgnoreCase(query, pageable)
                .map(this::toDto);
    }

    public TeamResponse getTeamByName(String name) {
        Team team = teamRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("Team with name '" + name + "' not found"));
        return toDto(team);
    }

    public List<String> getAllCountries() {
        return teamRepository.findAllDistinctCountries();
    }

    public List<String> getAllManufacturers() {
        return teamRepository.findAllDistinctManufacturers();
    }

    public boolean teamExists(String name) {
        return teamRepository.existsByNameIgnoreCase(name);
    }

    public Page<TeamResponse> getTeamsFoundedBetween(Integer startYear, Integer endYear, Pageable pageable) {
        return teamRepository.findByFoundedYearBetween(startYear, endYear, pageable)
                .map(this::toDto);
    }
}
