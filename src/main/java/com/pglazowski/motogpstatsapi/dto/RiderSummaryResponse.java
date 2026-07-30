package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Summary of rider information for nested responses.")
public record RiderSummaryResponse(
        @Schema(description = "Rider ID", example = "1")
        Long id,

        @Schema(description = "Rider's full name", example = "Francesco Bagnaia")
        String fullName,

        @Schema(description = "Rider's race number", example = "63")
        Integer raceNumber,

        @Schema(description = "Rider's nationality", example = "Italy")
        String nationality
) {}