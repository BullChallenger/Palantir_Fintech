package com.palantir.fintech.service;

import com.palantir.fintech.dto.EntryDTO.*;

public interface EntryService {

    Response create(Long applicationId, Request request);

    Response get(Long applicationId);

    UpdateResponse update(Long entryId, Request request);

    void delete(Long entryId);
}
