package com.palantir.fintech.controller;

import com.palantir.fintech.dto.EntryDTO.*;
import com.palantir.fintech.dto.RepaymentDTO;
import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.service.EntryService;
import com.palantir.fintech.service.RepaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/internal/applications")
@RequiredArgsConstructor
public class InternalController extends AbstractController {

    private final EntryService entryService;
    private final RepaymentService repaymentService;

    @PostMapping(value = "/{applicationId}/entries")
    public ResponseDTO<Response> create(@PathVariable(value = "applicationId") Long applicationId,
                                        @RequestBody Request request) {
        return ok(entryService.create(applicationId, request));
    }

    @GetMapping(value = "/{applicationId}/entries")
    public ResponseDTO<Response> get(@PathVariable(value = "applicationId") Long applicationId) {
        return ok(entryService.get(applicationId));
    }

    @PutMapping(value = "/entries/{entryId}")
    public ResponseDTO<UpdateResponse> update(@PathVariable(value = "entryId") Long entryId,
                                        @RequestBody Request request) {
        return ok(entryService.update(entryId, request));
    }

    @DeleteMapping(value = "/entries/{entryId}")
    public ResponseDTO<Void> delete(@PathVariable(value = "entryId") Long entryId) {
        entryService.delete(entryId);
        return ok();
    }

    @PostMapping(value = "/{applicationId}/repayments")
    public ResponseDTO<RepaymentDTO.Response> create(@PathVariable(value = "applicationId") Long applicationId,
                                                     @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.create(applicationId, request));
    }

    @GetMapping(value = "/{applicationId}/repayments")
    public ResponseDTO<List<RepaymentDTO.ListResponse>> getPayments(@PathVariable(value = "applicationId") Long applicationId) {
        return ok(repaymentService.get(applicationId));
    }

    @PutMapping(value = "/repayments/{repaymentId}")
    public ResponseDTO<RepaymentDTO.UpdateResponse> update(@PathVariable(value = "repaymentId") Long repaymentId,
                                                           @RequestBody RepaymentDTO.Request request) {
        return ok(repaymentService.update(repaymentId, request));
    }

    @DeleteMapping(value = "/repayments/{repaymentId}")
    public ResponseDTO<Void> deleteRepayment(@PathVariable(value = "repaymentId") Long repaymentId) {
        repaymentService.delete(repaymentId);
        return ok();
    }
}
