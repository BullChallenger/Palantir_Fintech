package com.palantir.fintech.service;


import com.palantir.fintech.dto.ApplicationDTO.*;
import com.palantir.fintech.dto.ApplicationDTO.Request;
import com.palantir.fintech.dto.ApplicationDTO.Response;

public interface ApplicationService {

    Response create(Request request);

    Response get(Long applicationId);

    Response update(Long applicationId, Request request);

    void delete(Long applicationId);

    Boolean acceptTerms(Long applicationId, AcceptTermsDTO request);
}
