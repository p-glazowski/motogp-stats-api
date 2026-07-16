package com.pglazowski.motogpstatsapi.dto;

public record TrackResponse(
        Long id,
        String gpName,
        String circuitName,
        String country,
        String city,
        Double lengthKm,
        Integer numberOfTurns,
        Integer raceLaps,
        String lapRecordTime,
        String lapRecordHolder,
        Integer lapRecordYear,
        Integer firstHeldYear
) {
}
