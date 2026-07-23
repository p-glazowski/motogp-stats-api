package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.URL;

@Schema(description = "Request payload used to create or update a MotoGP team.")
public record TeamRequest(
        @Schema(
                description = "Full official team name",
                example = "Ducati Lenovo Team"
        )
        @NotBlank(message = "Team name is required")
        String name,

        @Schema(
                description = "Motorcycle manufacturer",
                example = "Ducati"
        )
        @NotBlank(message = "Manufacturer is required")
        String manufacturer,

        @Schema(
                description = "Country where the team is registered",
                example = "Italy"
        )
        @NotBlank(message = "Country is required")
        String country,

        @Schema(
                description = "Team principal or manager",
                example = "Luigi Dall'Igna"
        )
        String teamPrincipal,

        @Schema(
                description = "Base location of the team",
                example = "Bologna, Italy"
        )
        String baseLocation,

        @Schema(
                description = "Year the team was founded",
                example = "1999"
        )
        @Positive(message = "Founded year must be a positive number")
        Integer foundedYear,

        @Schema(
                description = "Official team website",
                example = "https://www.ducati.com/racing"
        )
        @URL(message = "Website must be a valid URL")
        String website
) {}