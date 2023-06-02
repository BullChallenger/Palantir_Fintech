package com.palantir.fintech.service;

import static org.assertj.core.api.Assertions.*;

import com.palantir.fintech.domain.AcceptTerms;
import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Terms;
import com.palantir.fintech.dto.ApplicationDTO.*;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.repository.AcceptTermsRepository;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.TermsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @InjectMocks
    private ApplicationServiceImpl applicationService;

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private TermsRepository termsRepository;

    @Mock
    private AcceptTermsRepository acceptTermsRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_Return_Response_Of_Application_Entity() {
        Application theApplication = Application.builder()
                                                    .name("Member Kang")
                                                    .cellPhone("010-1111-2222")
                                                    .email("test@test.com")
                                                    .hopeAmount(BigDecimal.valueOf(50_000_000))
                                                .build();

        Request request = Request.builder()
                                    .name("Member Kang")
                                    .cellPhone("010-1111-2222")
                                    .email("test@test.com")
                                    .hopeAmount(BigDecimal.valueOf(50_000_000))
                                  .build();

        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(theApplication);

        Response response = applicationService.create(request);

        assertThat(response.getHopeAmount()).isSameAs(theApplication.getHopeAmount());
        assertThat(response.getEmail()).isSameAs(theApplication.getEmail());
    }

    @Test
    void Should_Return_Response_Of_Exist_Application_Entity() {
        Long applicationId = 1L;
        Application theApplication = Application.builder()
                                                    .applicationId(applicationId)
                                                    .name("Member Kang")
                                                    .cellPhone("010-1111-2222")
                                                    .email("test@test.com")
                                                    .hopeAmount(BigDecimal.valueOf(50_000_000))
                                                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(theApplication));
        Response actual = applicationService.get(applicationId);

        assertThat(theApplication.getApplicationId()).isSameAs(actual.getApplicationId());
        assertThat(theApplication.getHopeAmount()).isSameAs(actual.getHopeAmount());
    }

    @Test
    void Should_Return_Updated_Response_Of_Exist_Application_Entity() {
        Long applicationId = 1L;
        Application theApplication = Application.builder()
                                                    .applicationId(applicationId)
                                                    .name("Member Kang")
                                                    .cellPhone("010-1111-2222")
                                                    .email("test@test.com")
                                                    .hopeAmount(BigDecimal.valueOf(50_000_000))
                                                .build();
        Request request = Request.builder()
                                    .name("Member Kim")
                                    .cellPhone("010-2222-2222")
                                    .hopeAmount(BigDecimal.valueOf(40_000_000))
                                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(theApplication));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(theApplication);

        Response actual = applicationService.update(applicationId, request);

        assertThat(theApplication.getApplicationId()).isSameAs(actual.getApplicationId());
        assertThat(actual.getName()).isSameAs(request.getName());
        assertThat(actual.getCellPhone()).isSameAs(request.getCellPhone());
        assertThat(actual.getHopeAmount()).isSameAs(request.getHopeAmount());
    }

    @Test
    void Should_Return_Nothing_When_Application_Deleted() {
        Long applicationId = 1L;
        Application theApplication = Application.builder()
                                                    .applicationId(applicationId)
                                                    .name("Member Kang")
                                                    .cellPhone("010-1111-2222")
                                                    .email("test@test.com")
                                                    .hopeAmount(BigDecimal.valueOf(50_000_000))
                                                .build();
        when(applicationRepository.findById(applicationId)).thenReturn(Optional.ofNullable(theApplication));
        when(applicationRepository.save(theApplication)).thenReturn(theApplication);
        applicationService.delete(applicationId);

        assertThat(theApplication.getIsDeleted()).isTrue();
    }

    @Test
    void Should_Add_AcceptTerms_When_Application_Accept() {
        Terms theTerms01 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Terms theTerms02 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Long applicationId = 1L;
        List<Long> acceptTermsIds = Arrays.asList(1L, 2L);

        AcceptTermsDTO request = AcceptTermsDTO.builder()
                        .acceptTermsIds(acceptTermsIds)
                        .build();

        when(applicationRepository.findById(applicationId)).thenReturn(
                Optional.ofNullable(Application.builder().build())
        );
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(
                Arrays.asList(theTerms01, theTerms02)
        );
        when(acceptTermsRepository.save(ArgumentMatchers.any(AcceptTerms.class))).thenReturn(
                AcceptTerms.builder().build()
        );

        Boolean actual = applicationService.acceptTerms(applicationId, request);
        assertThat(actual).isTrue();
    }

    @Test
    void Should_Throw_Exception_When_Application_Not_Accept_All() {
        Terms theTerms01 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Terms theTerms02 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Long applicationId = 1L;
        List<Long> acceptTermsIds = Arrays.asList(1L);

        AcceptTermsDTO request = AcceptTermsDTO.builder()
                .acceptTermsIds(acceptTermsIds)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(
                Optional.ofNullable(Application.builder().build())
        );
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(
                Arrays.asList(theTerms01, theTerms02)
        );

        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }

    @Test
    void Should_Throw_Exception_When_Application_Accept_Not_Exist() {
        Terms theTerms01 = Terms.builder()
                .termsId(1L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Terms theTerms02 = Terms.builder()
                .termsId(2L)
                .name("대출 이용 약관")
                .termsDetailUrl("https://abc-storage.acc/asqygjqwneui")
                .build();

        Long applicationId = 1L;
        List<Long> acceptTermsIds = Arrays.asList(1L, 3L);

        AcceptTermsDTO request = AcceptTermsDTO.builder()
                .acceptTermsIds(acceptTermsIds)
                .build();

        when(applicationRepository.findById(applicationId)).thenReturn(
                Optional.ofNullable(Application.builder().build())
        );
        when(termsRepository.findAll(Sort.by(Sort.Direction.ASC, "termsId"))).thenReturn(
                Arrays.asList(theTerms01, theTerms02)
        );

        assertThrows(BaseException.class, () -> applicationService.acceptTerms(applicationId, request));
    }
}
