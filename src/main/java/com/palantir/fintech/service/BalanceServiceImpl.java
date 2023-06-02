package com.palantir.fintech.service;

import com.palantir.fintech.domain.Balance;
import com.palantir.fintech.dto.BalanceDTO.*;
import com.palantir.fintech.dto.BalanceDTO.RepaymentRequest.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.BalanceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BalanceServiceImpl implements BalanceService {

    private final BalanceRepository balanceRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        Balance theBalance = modelMapper.map(request, Balance.class);
        BigDecimal entryAmount = request.getEntryAmount();
        theBalance.setApplicationId(applicationId);
        theBalance.setBalance(entryAmount);

        balanceRepository.findByApplicationId(applicationId).ifPresent(balance -> {
            theBalance.setBalanceId(balance.getBalanceId());
            theBalance.setIsDeleted(balance.getIsDeleted());
            theBalance.setCreatedAt(balance.getCreatedAt());
            theBalance.setUpdatedAt(balance.getUpdatedAt());
        });

        Balance savedBalance = balanceRepository.save(theBalance);

        return modelMapper.map(savedBalance, Response.class);
    }

    @Override
    public Response update(Long applicationId, UpdateRequest request) {
        Balance theBalance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        BigDecimal beforeEntryAmount = request.getBeforeEntryAmount();
        BigDecimal afterEntryAmount = request.getAfterEntryAmount();
        BigDecimal updatedBalance = theBalance.getBalance();

        updatedBalance = updatedBalance.subtract(beforeEntryAmount).add(afterEntryAmount);
        theBalance.setBalance(updatedBalance);

        Balance updated = balanceRepository.save(theBalance);

        return modelMapper.map(updated, Response.class);
    }

    @Override
    public void delete(Long applicationId) {
        Balance balance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        balance.setIsDeleted(true);

        balanceRepository.save(balance);
    }

    @Override
    public Response repaymentUpdate(Long applicationId, RepaymentRequest request) {
        Balance theBalance = balanceRepository.findByApplicationId(applicationId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        BigDecimal updatedBalance = theBalance.getBalance();
        BigDecimal repaymentAmount = request.getRepaymentAmount();

        if (request.getType().equals(RepaymentType.ADD)) {
            updatedBalance = updatedBalance.add(repaymentAmount);
        } else {
            updatedBalance = updatedBalance.subtract(repaymentAmount);
        }

        theBalance.setBalance(updatedBalance);
        Balance updated = balanceRepository.save(theBalance);

        return modelMapper.map(updated, Response.class);
    }
}
