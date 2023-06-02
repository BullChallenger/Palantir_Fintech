package com.palantir.fintech.controller;

import com.palantir.fintech.domain.AcceptTerms;
import com.palantir.fintech.dto.ApplicationDTO.*;
import com.palantir.fintech.dto.FileDTO;
import com.palantir.fintech.dto.ResponseDTO;
import com.palantir.fintech.service.ApplicationService;
import com.palantir.fintech.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/applications")
@RequiredArgsConstructor
public class ApplicationController extends AbstractController {

    private final ApplicationService applicationService;
    private final FileStorageService fileStorageService;

    @PostMapping
    public ResponseDTO<Response> create(@RequestBody Request request) {
        return ok(applicationService.create(request));
    }

    @GetMapping(value = "/{applicationId}")
    public ResponseDTO<Response> get(@PathVariable("applicationId") Long applicationId) {
        return ok(applicationService.get(applicationId));
    }

    @PutMapping(value = "/{applicationId}")
    public ResponseDTO<Response> update(@PathVariable("applicationId") Long applicationId,
                                        @RequestBody Request request) {
        return ok(applicationService.update(applicationId, request));
    }

    @DeleteMapping(value = "/{applicationId}")
    public ResponseDTO<Response> delete(@PathVariable("applicationId") Long applicationId) {
        return ok();
    }

    @PostMapping(value = "/{applicationId}/terms")
    public ResponseDTO<Boolean> acceptTerms(@PathVariable("applicationId") Long applicationId,
                                             @RequestBody AcceptTermsDTO request) {
        return ok(applicationService.acceptTerms(applicationId, request));
    }

    @PostMapping(value = "/{applicationId}/files")
    public ResponseDTO<Void> upload(@PathVariable("applicationId") Long applicationId, MultipartFile file) {
        fileStorageService.save(applicationId, file);
        return ok();
    }

    @GetMapping(value = "/{applicationId}/files")
    public ResponseEntity<Resource> download(@PathVariable("applicationId") Long applicationId,
                                             @RequestParam(value = "fileName") String fileName) {
        Resource file = fileStorageService.load(applicationId, fileName);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @GetMapping(value = "/{applicationId}/files/info")
    public ResponseDTO<List<FileDTO>> getFileInfos(@PathVariable("applicationId") Long applicationId) {
        List<FileDTO> fileInfos = fileStorageService.loadAll(applicationId).map(path -> {
            String fileName = path.getFileName().toString();
            return FileDTO.builder()
                    .name(fileName)
                    .url(MvcUriComponentsBuilder.fromMethodName(ApplicationController.class,
                            "download",
                            fileName).build().toString())
                    .build();
        }).collect(Collectors.toList());
        return ok(fileInfos);
    }

    @DeleteMapping(value = "/{applicationId}/files")
    public ResponseDTO<Void> deleteAll(@PathVariable("applicationId") Long applicationId) {
        fileStorageService.deleteAll(applicationId);
        return ok();
    }

    @PutMapping(value = "/{applicationId}/contract")
    public ResponseDTO<Response> contract(@PathVariable(value = "applicationId") Long applicationId) {
        return ok(applicationService.contract(applicationId));
    }
}
