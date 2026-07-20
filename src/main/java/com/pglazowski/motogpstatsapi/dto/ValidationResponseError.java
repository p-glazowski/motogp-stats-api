package com.pglazowski.motogpstatsapi.dto;

import java.time.LocalDateTime;
import java.util.Map;

public record ValidationResponseError(int status,
                                      String error,
                                      String message,
                                      LocalDateTime timestamp,
                                      Map<String, String> validationErrors) {
}
