package com.palantir.fintech.controller;

import static com.palantir.fintech.dto.ResponseDTO.ok;
import static com.palantir.fintech.dto.TermsDTO.*;

import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.service.TermsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/terms")
@RequiredArgsConstructor
public class TermsController {

    private final TermsService termsService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(termsService.create(request));
    }

    @GetMapping
    public ResponseDTO<List<Response>> get() {
        return ok(termsService.getAll());
    }
}
