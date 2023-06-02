package com.palantir.fintech.service;

import com.palantir.fintech.dto.BalanceDTO.*;
import com.palantir.fintech.dto.BalanceDTO.RepaymentRequest.*;

public interface BalanceService {

    Response create(Long applicationId, Request request);

    Response update(Long applicationId, UpdateRequest request);

    void delete(Long applicationId);

    Response repaymentUpdate(Long applicationId, RepaymentRequest request);
}
