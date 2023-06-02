package com.palantir.fintech.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class FileDTO implements Serializable {

    private String name;

    private String url;

    @Builder
    public FileDTO(String name, String url) {
        this.name = name;
        this.url = url;
    }
}
