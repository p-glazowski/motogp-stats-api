package com.pglazowski.motogpstatsapi.services;

import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.dto.TeamResponse;
import com.pglazowski.motogpstatsapi.dto.TrackRequest;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.exceptions.NotFoundException;
import com.pglazowski.motogpstatsapi.models.Team;
import com.pglazowski.motogpstatsapi.models.Track;
import com.pglazowski.motogpstatsapi.repositories.TeamRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
}
