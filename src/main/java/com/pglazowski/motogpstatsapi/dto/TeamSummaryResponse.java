package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary of team information for nested responses.")
public record TeamSummaryResponse(
        @Schema(description = "Team ID", example = "1")
        Long id,

        @Schema(description = "Team name", example = "Ducati Lenovo Team")
        String name,

        @Schema(description = "Team manufacturer", example = "Ducati")
        String manufacturer,

        @Schema(description = "Team country", example = "Italy")
        String country
) {}
