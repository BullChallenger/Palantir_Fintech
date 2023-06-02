package com.palantir.fintech.service;

import com.palantir.fintech.dto.TermsDTO.*;

import java.util.List;

public interface TermsService {

    Response create(Request request);

    List<Response> getAll();
}
