package com.palantir.fintech.controller;

import com.palantir.fintech.dto.ApplicationDTO.GrantAmount;
import com.palantir.fintech.dto.JudgementDTO.*;
import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.service.JudgementService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/judgements")
public class JudgementController extends AbstractController {

    private final JudgementService judgementService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(judgementService.create(request));
    }

    @GetMapping(value = "/{applicationId}")
    public ResponseDTO<Response> get(@PathVariable("applicationId") Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @GetMapping(value = "/applications/{applicationId}")
    public ResponseDTO<Response> getJudgementOfApplication(@PathVariable("applicationId") Long applicationId) {
        return ok(judgementService.getJudgementOfApplication(applicationId));
    }

    @PutMapping(value = "/{judgementId}")
    public ResponseDTO<Response> update(@PathVariable("judgementId") Long judgementId,
                                        @RequestBody Request request) {
        return ok(judgementService.update(judgementId, request));
    }

    @DeleteMapping(value = "/{judgementId}")
    public ResponseDTO<Void> delete(@PathVariable("judgementId") Long judgementId) {
        judgementService.delete(judgementId);
        return ok();
    }

    @PatchMapping(value = "/{judgementId}/grants")
    public ResponseDTO<GrantAmount> grant(@PathVariable("judgementId") Long judgementId) {
        return ok(judgementService.grant(judgementId));
    }
}
