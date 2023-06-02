package com.palantir.fintech.service;

import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Judgement;
import com.palantir.fintech.dto.ApplicationDTO.GrantAmount;
import com.palantir.fintech.dto.JudgementDTO.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.JudgementRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class JudgementServiceImpl implements JudgementService {

    private final ModelMapper modelMapper;

    private final JudgementRepository judgementRepository;
    private final ApplicationRepository applicationRepository;

    @Override
    public Response create(Request request) {
        Long applicationId = request.getApplicationId();
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Judgement theJudgement = modelMapper.map(request, Judgement.class);
        Judgement savedJudgement = judgementRepository.save(theJudgement);

        return modelMapper.map(savedJudgement, Response.class);
    }

    @Override
    public Response get(Long judgementId) {
        Judgement theJudgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );
        return modelMapper.map(theJudgement, Response.class);
    }

    @Override
    public Response getJudgementOfApplication(Long applicationId) {
        if (!isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        Judgement theJudgement = judgementRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );
        return modelMapper.map(theJudgement, Response.class);
    }

    @Override
    public Response update(Long judgementId, Request request) {
        Judgement theJudgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        theJudgement.setName(request.getName());
        theJudgement.setApprovalAmount(request.getApprovalAmount());

        judgementRepository.save(theJudgement);
        return modelMapper.map(theJudgement, Response.class);
    }

    @Override
    public void delete(Long judgementId) {
        Judgement theJudgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        theJudgement.setIsDeleted(true);
        judgementRepository.save(theJudgement);
    }

    @Override
    public GrantAmount grant(Long judgementId) {
        Judgement theJudgement = judgementRepository.findById(judgementId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );
        Long applicationId = theJudgement.getApplicationId();
        Application theApplication = applicationRepository.findById(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        BigDecimal approvalAmount = theJudgement.getApprovalAmount();
        theApplication.setApprovalAmount(approvalAmount);

        applicationRepository.save(theApplication);

        return modelMapper.map(theApplication, GrantAmount.class);
    }

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
