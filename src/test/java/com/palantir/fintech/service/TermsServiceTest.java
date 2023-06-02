package com.palantir.fintech.service;

import static com.palantir.fintech.dto.TermsDTO.*;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import com.palantir.fintech.domain.Terms;
import com.palantir.fintech.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TermsServiceTest {

    @Mock
    private TermsRepository termsRepository;

    @Spy
    private ModelMapper modelMapper;

    @InjectMocks
    private TermsServiceImpl termsService;

    @Test
    void Should_Return_Response_Of_Terms_Entity() {
        Terms theTerms = Terms.builder()
                                    .name("대출 이용 약관")
                                    .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                                .build();
        Request request = Request.builder()
                                    .name("대출 이용 약관")
                                    .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                                .build();

        when(termsRepository.save(ArgumentMatchers.any(Terms.class))).thenReturn(theTerms);
        Response actual = termsService.create(request);

        assertThat(request.getName()).isSameAs(actual.getName());
        assertThat(request.getTermsDetailUrl()).isSameAs(actual.getTermsDetailUrl());
    }

    @Test
    void Should_Return_All_Response_Of_Exist_Terms() {
        Terms theTerms01 = Terms.builder()
                                    .name("대출 이용 약관01")
                                    .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                                .build();
        Terms theTerms02 = Terms.builder()
                                    .name("대출 이용 약관02")
                                    .termsDetailUrl("https://abc-storage.acc/adqweqwwqwdi")
                                .build();
        List<Terms> termsList = List.of(theTerms01, theTerms02);
        when(termsRepository.findAll()).thenReturn(termsList);

        List<Response> actual = termsService.getAll();

        assertThat(actual.size()).isEqualTo(termsList.size());
    }
}
