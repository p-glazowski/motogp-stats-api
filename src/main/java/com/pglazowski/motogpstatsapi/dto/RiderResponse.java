package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Response payload containing MotoGP rider information.")
public record RiderResponse(
        @Schema(description = "Unique identifier of the rider", example = "1")
        Long id,

        @Schema(description = "Rider's first name", example = "Francesco")
        String firstName,

        @Schema(description = "Rider's last name", example = "Bagnaia")
        String lastName,

        @Schema(description = "Rider's full name", example = "Francesco Bagnaia")
        String fullName,

        @Schema(description = "Rider's nationality", example = "Italy")
        String nationality,

        @Schema(description = "Rider's age", example = "29")
        Integer age,

        @Schema(description = "Rider's race number", example = "63")
        Integer raceNumber,

        @Schema(description = "Team information (can be null if rider is not assigned to a team)")
        TeamSummaryResponse team,

        @Schema(description = "Number of MotoGP race wins", example = "25")
        Integer wins,

        @Schema(description = "Number of MotoGP podiums", example = "45")
        Integer podiums,

        @Schema(description = "Number of pole positions", example = "18")
        Integer polePositions,

        @Schema(description = "Number of World Championships", example = "2")
        Integer worldTitles,

        @Schema(description = "Rider's biography")
        String biography,

        @Schema(description = "URL to rider's profile image", example = "https://www.motogp.com/images/riders/bagnaia.jpg")
        String imageUrl,

        @Schema(description = "Timestamp when the rider was created")
        LocalDateTime createdAt
) {}
