package com.palantir.fintech.service;

import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Terms;
import com.palantir.fintech.dto.ApplicationDTO.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.AcceptTermsRepository;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.JudgementRepository;
import com.palantir.fintech.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ModelMapper modelMapper;

    private final ApplicationRepository applicationRepository;
    private final JudgementRepository judgementRepository;
    private final TermsRepository termsRepository;
    private final AcceptTermsRepository acceptTermsRepository;

    @Override
    public Response create(Request request) {
        Application theApplication = modelMapper.map(request, Application.class);
        theApplication.setAppliedAt(LocalDateTime.now());

        Application appliedApp = applicationRepository.save(theApplication);
        return modelMapper.map(appliedApp, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Application theApplication = applicationRepository.findById(applicationId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR)
        );
        return modelMapper.map(theApplication, Response.class);
    }

    @Override
    public Response update(Long applicationId, Request request) {
        Application theApplication = applicationRepository.findById(applicationId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR)
        );

        theApplication.setName(request.getName());
        theApplication.setCellPhone(request.getCellPhone());
        theApplication.setEmail(request.getEmail());
        theApplication.setHopeAmount(request.getHopeAmount());

        applicationRepository.save(theApplication);
        return modelMapper.map(theApplication, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Application theApplication = applicationRepository.findById(applicationId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR)
        );
        theApplication.setIsDeleted(true);
        applicationRepository.save(theApplication);
    }

    @Override
    public Boolean acceptTerms(Long applicationId, AcceptTermsDTO request) {
        applicationRepository.findById(applicationId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR)
        );
        List<Terms> termsList = termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"));
        if (termsList.isEmpty()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> acceptTermsIds = request.getAcceptTermsIds();
        if (termsList.size() != acceptTermsIds.size()) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        List<Long> termsIds = termsList.stream().map(Terms::getTermsId).collect(Collectors.toList());
        Collections.sort(acceptTermsIds);
        if (!termsIds.containsAll(acceptTermsIds)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        for (Long termsId : acceptTermsIds) {
            com.palantir.fintech.domain.AcceptTerms theAcceptTerms = com.palantir.fintech.domain.AcceptTerms.builder()
                            .termsId(termsId)
                            .applicationId(applicationId)
                        .build();

            acceptTermsRepository.save(theAcceptTerms);
        }

        return true;
    }

    @Override
    public Response contract(Long applicationId) {
        Application theApplication = applicationRepository.findById(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        judgementRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        if (theApplication.getApprovalAmount() == null ||
                theApplication.getApprovalAmount().compareTo(BigDecimal.ZERO) == 0) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        theApplication.setContractedAt(LocalDateTime.now());
        applicationRepository.save(theApplication);

        return null;
    }
}
