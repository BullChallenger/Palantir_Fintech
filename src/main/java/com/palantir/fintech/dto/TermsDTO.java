package com.palantir.fintech.dto;

import lombok.*;

import java.time.LocalDateTime;

public class TermsDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {
        private String name;
        private String termsDetailUrl;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private Long termsId;

        private String name;
        private String termsDetailUrl;

        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
    }
}
