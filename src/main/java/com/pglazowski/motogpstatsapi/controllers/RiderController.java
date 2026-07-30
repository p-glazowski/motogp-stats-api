package com.pglazowski.motogpstatsapi.controllers;

import com.pglazowski.motogpstatsapi.dto.RiderRequest;
import com.pglazowski.motogpstatsapi.dto.RiderResponse;
import com.pglazowski.motogpstatsapi.services.RiderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
        name = "Riders",
        description = "MotoGP riders management API"
)
@RestController
@RequestMapping("/riders")
public class RiderController {

    private final RiderService riderService;

    public RiderController(RiderService riderService) {
        this.riderService = riderService;
    }

    @Operation(
            summary = "Get riders with filtering, pagination and sorting",
            description = """
                    Returns riders with optional filtering by nationality, team, or search term.
                    
                    Supports pagination and sorting.
                    
                    Examples:
                    - /riders?nationality=Italy
                    - /riders?teamId=1
                    - /riders?search=Marquez
                    - /riders?page=0&size=5
                    - /riders?sort=wins,desc
                    - /riders?nationality=Spain&page=0&size=5&sort=wins,desc
                    """
    )
    @ApiResponse(
            responseCode = "200",
            description = "Riders retrieved successfully"
    )
    @GetMapping
    public Page<RiderResponse> getRiders(
            @Parameter(
                    description = "Filter riders by nationality",
                    example = "Italy",
                    schema = @Schema(type = "string")
            )
            @RequestParam(required = false) String nationality,

            @Parameter(
                    description = "Filter riders by team ID",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @RequestParam(required = false) Long teamId,

            @Parameter(
                    description = "Search riders by full name (partial match)",
                    example = "Marquez",
                    schema = @Schema(type = "string")
            )
            @RequestParam(required = false) String search,

            @ParameterObject
            @PageableDefault(size = 10, sort = "id")
            Pageable pageable
    ) {
        return riderService.getRiders(nationality, teamId, search, pageable);
    }

    @Operation(
            summary = "Get rider by ID",
            description = "Returns a single rider for the given database ID."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Rider found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Rider not found"
    )
    @GetMapping("/{id}")
    public RiderResponse getRiderById(
            @Parameter(
                    description = "Rider database ID",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id
    ) {
        return riderService.getRiderById(id);
    }

    @Operation(
            summary = "Find rider by full name",
            description = "Returns a rider matching the specified full name (exact match, case-insensitive)."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Rider found"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Rider not found"
    )
    @GetMapping("/name")
    public RiderResponse getRiderByFullName(
            @Parameter(
                    description = "Rider's full name",
                    example = "Francesco Bagnaia",
                    schema = @Schema(type = "string")
            )
            @RequestParam String fullName
    ) {
        return riderService.getRiderByFullName(fullName);
    }

    @Operation(
            summary = "Get all distinct nationalities",
            description = "Returns a list of all nationalities represented in the riders data, useful for filter dropdowns."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Nationalities retrieved successfully",
            content = @Content(
                    mediaType = "application/json",
                    array = @ArraySchema(
                            schema = @Schema(
                                    type = "string",
                                    description = "Nationality name",
                                    example = "[\"Italy\", \"Spain\", \"France\", \"South Africa\", \"Australia\", \"Turkey\", \"Japan\", \"Brazil\"]"
                            ),
                            minItems = 1

                    )
            )
    )
    @GetMapping("/nationalities")
    public List<String> getAllNationalities() {
        return riderService.getAllNationalities();
    }

    @Operation(
            summary = "Check if rider exists",
            description = "Checks if a rider with the specified full name already exists."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Check completed successfully"
    )
    @GetMapping("/exists")
    public boolean riderExists(
            @Parameter(
                    description = "Rider's full name to check",
                    example = "Francesco Bagnaia",
                    schema = @Schema(type = "string")
            )
            @RequestParam String fullName
    ) {
        return riderService.riderExists(fullName);
    }

    @Operation(
            summary = "Create a new rider",
            description = "Creates a new MotoGP rider and returns the created resource."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Rider created successfully"
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
            description = "Rider with this name or race number already exists"
    )
    @PostMapping
    public RiderResponse createRider(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Rider data to create",
                    required = true
            )
            @RequestBody @Valid RiderRequest request
    ) {
        return riderService.createRider(request);
    }

    @Operation(
            summary = "Update an existing rider",
            description = "Updates all information for the specified rider."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Rider updated successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid request data"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Rider or team not found"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Rider with this name or race number already exists"
    )
    @PutMapping("/{id}")
    public RiderResponse updateRider(
            @Parameter(
                    description = "Rider database ID",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id,

            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated rider data",
                    required = true
            )
            @RequestBody @Valid RiderRequest request
    ) {
        return riderService.updateRider(id, request);
    }

    @Operation(
            summary = "Delete a rider",
            description = "Deletes the rider with the specified ID."
    )
    @ApiResponse(
            responseCode = "204",
            description = "Rider deleted successfully"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Rider not found"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRider(
            @Parameter(
                    description = "Rider database ID",
                    example = "1",
                    schema = @Schema(type = "integer", format = "int64")
            )
            @PathVariable Long id
    ) {
        riderService.deleteRider(id);
        return ResponseEntity.noContent().build();
    }
}
