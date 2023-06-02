package com.palantir.fintech.dto;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class EntryDTO implements Serializable {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private BigDecimal entryAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long entryId;
        private Long applicationId;

        private BigDecimal entryAmount;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponse {
        private Long entryId;
        private Long applicationId;

        private BigDecimal beforeEntryAmount;
        private BigDecimal afterEntryAmount;
    }

}
