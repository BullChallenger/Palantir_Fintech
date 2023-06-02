package com.palantir.fintech.service;

import com.palantir.fintech.dto.ApplicationDTO.GrantAmount;
import com.palantir.fintech.dto.JudgementDTO.*;

public interface JudgementService {

    Response create(Request request);

    Response get(Long judgementId);

    Response getJudgementOfApplication(Long applicationId);

    Response update(Long judgementId, Request request);

    void delete(Long judgementId);

    GrantAmount grant(Long judgementId);
}
