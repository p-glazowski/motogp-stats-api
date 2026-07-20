package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "MotoGP track details returned by the API.")
public record TrackResponse(
        @Schema(
                description = "Unique database identifier",
                example = "1"
        )
        Long id,
        @Schema(
                description = "Official MotoGP Grand Prix name",
                example = "Italian Grand Prix"
        )
        String gpName,
        @Schema(
                description = "Name of the racing circuit",
                example = "Mugello Circuit"
        )
        String circuitName,
        @Schema(
                description = "Country where the circuit is located",
                example = "Italy"
        )
        String country,
        @Schema(
                description = "City where the circuit is located",
                example = "Scarperia e San Piero"
        )
        String city,
        @Schema(
                description = "Circuit length in kilometers",
                example = "5.245"
        )
        Double lengthKm,
        @Schema(
                description = "Total number of turns",
                example = "15"
        )
        Integer numberOfTurns,
        @Schema(
                description = "Number of laps in the MotoGP race",
                example = "23"
        )
        Integer raceLaps,
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
) {
}
