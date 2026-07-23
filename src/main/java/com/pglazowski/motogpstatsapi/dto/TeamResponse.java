package com.pglazowski.motogpstatsapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response payload containing MotoGP team information.")
public record TeamResponse(
        @Schema(description = "Unique identifier of the team", example = "1")
        Long id,

        @Schema(description = "Full official team name", example = "Ducati Lenovo Team")
        String name,

        @Schema(description = "Motorcycle manufacturer", example = "Ducati")
        String manufacturer,

        @Schema(description = "Country where the team is registered", example = "Italy")
        String country,

        @Schema(description = "Team principal or manager", example = "Luigi Dall'Igna")
        String teamPrincipal,

        @Schema(description = "Base location of the team", example = "Bologna, Italy")
        String baseLocation,

        @Schema(description = "Year the team was founded", example = "1999")
        Integer foundedYear,

        @Schema(description = "Official team website", example = "https://www.ducati.com/racing")
        String website
) {}