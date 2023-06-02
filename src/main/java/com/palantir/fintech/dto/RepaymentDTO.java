package com.palantir.fintech.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RepaymentDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private BigDecimal repaymentAmount;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long applicationId;

        private BigDecimal repaymentAmount;
        private BigDecimal balance;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ListResponse {
        private Long repaymentId;

        private BigDecimal repaymentAmount;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateResponse {
        private Long applicationId;

        private BigDecimal beforeRepaymentAmount;
        private BigDecimal afterRepaymentAmount;
        private BigDecimal balance;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
