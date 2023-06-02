package com.palantir.fintech.service;

import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Entry;
import com.palantir.fintech.domain.Repayment;
import com.palantir.fintech.dto.BalanceDTO;
import com.palantir.fintech.dto.BalanceDTO.RepaymentRequest.RepaymentType;
import com.palantir.fintech.dto.BalanceDTO.RepaymentRequest;
import com.palantir.fintech.dto.RepaymentDTO.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.EntryRepository;
import com.palantir.fintech.repository.RepaymentRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RepaymentServiceImpl implements RepaymentService {

    private final RepaymentRepository repaymentRepository;
    private final ApplicationRepository applicationRepository;
    private final EntryRepository entryRepository;

    private final BalanceService balanceService;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        if (!isRepayableApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Repayment theRepayment = modelMapper.map(request, Repayment.class);
        theRepayment.setApplicationId(applicationId);

        repaymentRepository.save(theRepayment);

        BalanceDTO.Response updatedBalance = balanceService.repaymentUpdate(applicationId,
                RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(RepaymentType.REMOVE)
                        .build());

        Response response = modelMapper.map(theRepayment, Response.class);
        response.setBalance(updatedBalance.getBalance());

        return response;
    }

    @Override
    public List<ListResponse> get(Long applicationId) {
        List<Repayment> repayments = repaymentRepository.findAllByApplicationId(applicationId);
        return repayments.stream().map(repayment -> modelMapper.map(repayment, ListResponse.class)).collect(Collectors.toList());
    }

    @Override
    public UpdateResponse update(Long repaymentId, Request request) {
        Repayment theRepayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        Long applicationId = theRepayment.getApplicationId();
        BigDecimal beforeRepaymentAmount = theRepayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(beforeRepaymentAmount)
                        .type(RepaymentType.ADD)
                        .build());

        theRepayment.setRepaymentAmount(request.getRepaymentAmount());
        repaymentRepository.save(theRepayment);

        BalanceDTO.Response updated = balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(request.getRepaymentAmount())
                        .type(RepaymentType.REMOVE)
                        .build());

        return UpdateResponse.builder()
                .applicationId(applicationId)
                .beforeRepaymentAmount(beforeRepaymentAmount)
                .afterRepaymentAmount(request.getRepaymentAmount())
                .balance(updated.getBalance())
                .build();
    }

    @Override
    public void delete(Long repaymentId) {
        Repayment theRepayment = repaymentRepository.findById(repaymentId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        Long applicationId = theRepayment.getApplicationId();
        BigDecimal removeRepaymentAmount = theRepayment.getRepaymentAmount();

        balanceService.repaymentUpdate(applicationId,
                BalanceDTO.RepaymentRequest.builder()
                        .repaymentAmount(removeRepaymentAmount)
                        .type(RepaymentType.ADD)
                        .build());

        theRepayment.setIsDeleted(true);
        repaymentRepository.save(theRepayment);
    }

    private boolean isRepayableApplication(Long applicationId) {
        Optional<Application> existApplication = applicationRepository.findById(applicationId);
        if (existApplication.isEmpty()) {
            return false;
        }
        if (existApplication.get().getContractedAt() == null) {
            return false;
        }

        Optional<Entry> existEntry = entryRepository.findByApplicationId(applicationId);
        return existEntry.isPresent();
    }
}
