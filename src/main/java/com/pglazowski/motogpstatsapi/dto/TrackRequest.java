package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(description = "Request payload used to create or update a MotoGP track.")
public record TrackRequest(
        @Schema(
                description = "Official MotoGP Grand Prix name",
                example = "Italian Grand Prix"
        )
        @NotBlank String gpName,
        @Schema(
                description = "Name of the racing circuit",
                example = "Mugello Circuit"
        )
        @NotBlank String circuitName,
        @Schema(
                description = "Country where the circuit is located",
                example = "Italy"
        )
        @NotBlank String country,
        @Schema(
                description = "City where the circuit is located",
                example = "Scarperia e San Piero"
        )
        @NotBlank String city,
        @Schema(
                description = "Circuit length in kilometers",
                example = "5.245"
        )
        @NotNull @Positive Double lengthKm,
        @Schema(
                description = "Total number of turns",
                example = "15"
        )
        @NotNull @Positive Integer numberOfTurns,
        @Schema(
                description = "Number of laps in the MotoGP race",
                example = "23"
        )
        @NotNull @Positive Integer raceLaps,
        @Schema(
                description = "Fastest recorded lap time",
                example = "1:45.187"
        )
        String lapRecordTime,
        @Schema(
                description = "Rider holding the current lap record",
                example = "Francesco Bagnaia"
        )
        String lapRecordHolder,
        @Schema(
                description = "Year when the lap record was set",
                example = "2023"
        )
        Integer lapRecordYear,
        @Schema(
                description = "First year the MotoGP Grand Prix was held at this circuit",
                example = "1974"
        )
        Integer firstHeldYear
) {}