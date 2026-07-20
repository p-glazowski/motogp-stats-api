package com.pglazowski.motogpstatsapi.dto;

import java.time.LocalDateTime;

public record ResponseError(
        int status,
        String error,
        String message,
        LocalDateTime localDateTime
) {
}
