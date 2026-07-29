package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

@Schema(description = "Request payload used to create or update a MotoGP rider.")
public record RiderRequest(
        @Schema(
                description = "Rider's first name",
                example = "Francesco",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name must not exceed 100 characters")
        String firstName,

        @Schema(
                description = "Rider's last name",
                example = "Bagnaia",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Last name is required")
        @Size(max = 100, message = "Last name must not exceed 100 characters")
        String lastName,

        @Schema(
                description = "Rider's full name (must be unique)",
                example = "Francesco Bagnaia",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Full name is required")
        @Size(max = 200, message = "Full name must not exceed 200 characters")
        String fullName,

        @Schema(
                description = "Rider's nationality",
                example = "Italy",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(message = "Nationality is required")
        @Size(max = 100, message = "Nationality must not exceed 100 characters")
        String nationality,

        @Schema(
                description = "Rider's age",
                example = "29",
                requiredMode = Schema.RequiredMode.REQUIRED,
                minimum = "16",
                maximum = "65"
        )
        @NotNull(message = "Age is required")
        @Min(value = 16, message = "Age must be at least 16")
        @Max(value = 65, message = "Age must not exceed 65")
        Integer age,

        @Schema(
                description = "Rider's race number (must be unique)",
                example = "63",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Race number is required")
        @Positive(message = "Race number must be a positive number")
        @Max(value = 999, message = "Race number must not exceed 999")
        Integer raceNumber,

        @Schema(
                description = "ID of the team the rider belongs to",
                example = "1",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Team ID is required")
        @Positive(message = "Team ID must be a positive number")
        Long teamId,

        @Schema(
                description = "Number of MotoGP race wins",
                example = "25",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Wins is required")
        @Min(value = 0, message = "Wins cannot be negative")
        Integer wins,

        @Schema(
                description = "Number of MotoGP podiums",
                example = "45",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Podiums is required")
        @Min(value = 0, message = "Podiums cannot be negative")
        Integer podiums,

        @Schema(
                description = "Number of pole positions",
                example = "18",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "Pole positions is required")
        @Min(value = 0, message = "Pole positions cannot be negative")
        Integer polePositions,

        @Schema(
                description = "Number of World Championships",
                example = "2",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(message = "World titles is required")
        @Min(value = 0, message = "World titles cannot be negative")
        Integer worldTitles,

        @Schema(
                description = "Rider's biography",
                example = "Two-time MotoGP World Champion. Known for his incredible racing intelligence and consistency."
        )
        @Size(max = 10000, message = "Biography must not exceed 10000 characters")
        String biography,

        @Schema(
                description = "URL to rider's profile image",
                example = "https://www.motogp.com/images/riders/bagnaia.jpg"
        )
        @Size(max = 255, message = "Image URL must not exceed 255 characters")
        @Pattern(
                regexp = "^(https?://).*$",
                message = "Image URL must be a valid HTTP or HTTPS URL"
        )
        String imageUrl
) {}
