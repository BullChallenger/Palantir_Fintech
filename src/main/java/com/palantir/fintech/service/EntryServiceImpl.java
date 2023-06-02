package com.palantir.fintech.service;

import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Entry;
import com.palantir.fintech.dto.BalanceDTO;
import com.palantir.fintech.dto.EntryDTO.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.EntryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EntryServiceImpl implements EntryService {

    private final EntryRepository entryRepository;
    private final BalanceService balanceService;
    private final ApplicationRepository applicationRepository;

    private final ModelMapper modelMapper;

    @Override
    public Response create(Long applicationId, Request request) {
        if (!isContractedApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

        Entry theEntry = modelMapper.map(request, Entry.class);
        theEntry.setApplicationId(applicationId);

        entryRepository.save(theEntry);
        balanceService.create(applicationId,
                BalanceDTO.Request.builder()
                        .entryAmount(request.getEntryAmount())
                        .build());

        return modelMapper.map(theEntry, Response.class);
    }

    @Override
    public Response get(Long applicationId) {
        Optional<Entry> theEntry = entryRepository.findByApplicationId(applicationId);

        if (theEntry.isPresent()) {
            return modelMapper.map(theEntry, Response.class);
        } else {
            return null;
        }
    }

    @Override
    public UpdateResponse update(Long entryId, Request request) {
        Entry theEntry = entryRepository.findById(entryId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        BigDecimal beforeEntryAmount = theEntry.getEntryAmount();
        theEntry.setEntryAmount(request.getEntryAmount());

        entryRepository.save(theEntry);

        Long applicationId = theEntry.getApplicationId();
        balanceService.update(applicationId,
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(request.getEntryAmount())
                        .build());

        return UpdateResponse.builder()
                .entryId(entryId)
                .applicationId(applicationId)
                .beforeEntryAmount(beforeEntryAmount)
                .afterEntryAmount(request.getEntryAmount())
                .build();
    }

    @Override
    public void delete(Long entryId) {
        Entry theEntry = entryRepository.findById(entryId).orElseThrow(() ->
                new BaseException(ResultType.SYSTEM_ERROR)
        );

        theEntry.setIsDeleted(true);
        entryRepository.save(theEntry);

        BigDecimal beforeEntryAmount = theEntry.getEntryAmount();

        balanceService.update(theEntry.getApplicationId(),
                BalanceDTO.UpdateRequest.builder()
                        .beforeEntryAmount(beforeEntryAmount)
                        .afterEntryAmount(BigDecimal.ZERO)
                        .build());
    }

    private boolean isContractedApplication(Long applicationId) {
        Optional<Application> existApplication = applicationRepository.findById(applicationId);
        if (existApplication.isEmpty()) {
            return false;
        }

        return existApplication.get().getContractedAt() != null;
    }
}
