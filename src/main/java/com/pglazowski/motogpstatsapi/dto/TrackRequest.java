package com.pglazowski.motogpstatsapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TrackRequest(
        @NotBlank String gpName,
        @NotBlank String circuitName,
        @NotBlank String country,
        @NotBlank String city,
        @NotNull @Positive Double lengthKm,
        @NotNull @Positive Integer numberOfTurns,
        @NotNull @Positive Integer raceLaps,
        String lapRecordTime,
        String lapRecordHolder,
        Integer lapRecordYear,
        Integer firstHeldYear
) {}