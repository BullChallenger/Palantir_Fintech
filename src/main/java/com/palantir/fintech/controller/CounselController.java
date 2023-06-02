package com.palantir.fintech.controller;

import com.palantir.fintech.dto.CounselDTO.Response;
import com.palantir.fintech.dto.CounselDTO.Request;
import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.service.CounselService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/counsels")
@RequiredArgsConstructor
public class CounselController extends AbstractController {

    private final CounselService counselService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(counselService.create(request));
    }

    @GetMapping(value = "/{counselId}")
    public ResponseDTO<Response> get(@PathVariable("counselId") Long counselId) {
        return ok(counselService.get(counselId));
    }

    @PutMapping(value = "/{counselId}")
    public ResponseDTO<Response> update(@PathVariable("counselId") Long counselId,
                                        @RequestBody Request request) {
        return ok(counselService.update(counselId, request));
    }

    @DeleteMapping(value = "/{counselId}")
    public ResponseDTO<Response> delete(@PathVariable("counselId") Long counselId) {
        return ok();
    }
}
