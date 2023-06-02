package com.palantir.fintech.service;

import static com.palantir.fintech.dto.CounselDTO.Response;
import static com.palantir.fintech.dto.CounselDTO.Request;

public interface CounselService {

    Response create(Request request);

    Response get(Long counselId);

    Response update(Long counselId, Request request);

    void delete(Long counselId);
}
