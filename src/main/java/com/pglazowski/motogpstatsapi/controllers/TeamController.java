package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.TeamRequest;
import com.pglazowski.motogpstatsapi.dto.TeamResponse;
import com.pglazowski.motogpstatsapi.dto.TrackResponse;
import com.pglazowski.motogpstatsapi.services.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name="Teams",
        description = "MotoGP teams management API"
)
@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @Operation(
            summary = "Get teams with filtering, pagination and sorting",
            description = """
                Returns teams with optional filtering by country and manufacturer.
                
                Supports pagination and sorting.
                
                Examples:
                - /teams?country=Italy
                - /teams?manufacturer=Ducati
                - /teams?page=0&size=5
                - /teams?sort=foundedYear,desc
                - /teams?country=Italy&page=0&size=5&sort=foundedYear,desc
                """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Teams retrieved successfully"
    )
    @GetMapping
    public Page<TeamResponse> getTeams(
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String manufacturer,
            @ParameterObject
            @PageableDefault(size = 5, sort = "id")
            Pageable pageable
    ) {
        return teamService.getTeams(country, manufacturer, pageable);
    }

    @Operation(
            summary = "Get team by ID",
            description = "Returns a single team for the given database ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Team found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Team not found"
    )
    @GetMapping("/{id}")
    public TeamResponse getTeamById(@PathVariable Long id) {
        return teamService.getTeamById(id);
    }

    @Operation(
            summary = "Find team by name",
            description = "Returns a team matching the specified team name (exact match, case-insensitive)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Team found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Team not found"
    )
    @GetMapping("/name")
    public TeamResponse getTeamByName(@RequestParam String name) {
        return teamService.getTeamByName(name);
    }

    @Operation(
            summary = "Search teams by name",
            description = "Performs a case-insensitive partial search on team names."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Search completed successfully"
    )
    @GetMapping("/search")
    public Page<TeamResponse> searchTeams(
            @RequestParam String query,
            @ParameterObject
            @PageableDefault(size = 10, sort = "name")
            Pageable pageable
    ) {
        return teamService.searchTeams(query, pageable);
    }

    @Operation(
            summary = "Get teams founded between years",
            description = "Returns all teams founded within the specified year range (inclusive)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Teams retrieved successfully"
    )
    @GetMapping("/founded-between")
    public Page<TeamResponse> getTeamsFoundedBetween(
            @RequestParam Integer startYear,
            @RequestParam Integer endYear,
            @ParameterObject
            @PageableDefault(size = 10, sort = "foundedYear")
            Pageable pageable
    ) {
        return teamService.getTeamsFoundedBetween(startYear, endYear, pageable);
    }

    @Operation(
            summary = "Get all unique countries",
            description = "Returns a list of all countries represented in the teams data, useful for filter dropdowns."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Countries retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    type = "string",
                                    description = "Country name",
                                    example = "[\"Italy\", \"Japan\", \"Austria\", \"France\", \"USA\", \"Spain\"]"
                            ),
                            minItems = 1
                    )
            )
    )
    @GetMapping("/countries")
    public List<String> getAllCountries() {
        return teamService.getAllCountries();
    }

    @Operation(
            summary = "Get all unique manufacturers",
            description = "Returns a list of all manufacturers represented in the teams data, useful for filter dropdowns."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Manufacturers retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    type = "string",
                                    description = "Manufacturer name",
                                    example = "[\"Ducati\", \"Yamaha\", \"Honda\", \"KTM\", \"Aprilia\"]"
                            ),
                            minItems = 1
                    )
            )
    )
    @GetMapping("/manufacturers")
    public List<String> getAllManufacturers() {
        return teamService.getAllManufacturers();
    }

    @Operation(
            summary = "Check if team exists",
            description = "Checks if a team with the specified name already exists."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Check completed successfully"
    )
    @GetMapping("/exists")
    public boolean teamExists(@RequestParam String name) {
        return teamService.teamExists(name);
    }

    @Operation(
            summary = "Create a new team",
            description = "Creates a new MotoGP team and returns the created resource."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Team created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Team with this name already exists"
    )
    @PostMapping
    public TeamResponse createTeam(@RequestBody @Valid TeamRequest request) {
        return teamService.createTeam(request);
    }

    @Operation(
            summary = "Update an existing team",
            description = "Updates all information for the specified team."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Team updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Team not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Team with this name already exists"
    )
    @PutMapping("/{id}")
    public TeamResponse updateTeam(
            @PathVariable Long id,
            @RequestBody @Valid TeamRequest request
    ) {
        return teamService.updateTeam(id, request);
    }

    @Operation(
            summary = "Delete a team",
            description = "Deletes the team with the specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Team deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Team not found"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}
