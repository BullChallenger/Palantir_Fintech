package com.palantir.fintech.service;

import com.palantir.fintech.dto.RepaymentDTO.*;

import java.util.List;

public interface RepaymentService {

    Response create(Long applicationId, Request request);

    List<ListResponse> get(Long applicationId);

    UpdateResponse update(Long repaymentId, Request request);

    void delete(Long repaymentId);
}
