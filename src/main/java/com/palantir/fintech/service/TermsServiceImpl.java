package com.palantir.fintech.service;

import com.palantir.fintech.domain.Terms;
import com.palantir.fintech.dto.TermsDTO.*;
import com.palantir.fintech.repository.TermsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TermsServiceImpl implements TermsService {

    private final ModelMapper modelMapper;

    private final TermsRepository termsRepository;

    @Override
    public Response create(Request request) {
        Terms theTerms = modelMapper.map(request, Terms.class);
        Terms createdTerms = termsRepository.save(theTerms);

        return modelMapper.map(createdTerms, Response.class);
    }

    @Override
    public List<Response> getAll() {
        List<Terms> theTemrsList = termsRepository.findAll();
        return theTemrsList.stream().map(terms ->
            modelMapper.map(terms, Response.class)).collect(Collectors.toList());
    }
}
