package com.palantir.fintech.service;

import com.palantir.fintech.domain.Application;
import com.palantir.fintech.domain.Judgement;
import com.palantir.fintech.dto.ApplicationDTO.GrantAmount;
import com.palantir.fintech.dto.JudgementDTO.*;
import com.palantir.fintech.repository.ApplicationRepository;
import com.palantir.fintech.repository.JudgementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JudgementServiceTest {

    @InjectMocks
    private JudgementServiceImpl judgementService;

    @Mock
    private JudgementRepository judgementRepository;

    @Mock
    private ApplicationRepository applicationRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_Return_ResponseDTO_When_Request_New_Judgement() {
        Judgement theJudgement = Judgement.builder()
                .applicationId(1L)
                .name("Employee_Kim")
                .approvalAmount(BigDecimal.valueOf(5_000_000))
                .build();
        Request request = Request.builder()
                .applicationId(1L)
                .name("Employee_Kim")
                .approvalAmount(BigDecimal.valueOf(5_000_000))
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(theJudgement);

        Response actual = judgementService.create(request);
        assertThat(actual.getName()).isSameAs(theJudgement.getName());
        assertThat(actual.getApplicationId()).isSameAs(theJudgement.getApplicationId());
        assertThat(actual.getApprovalAmount()).isSameAs(theJudgement.getApprovalAmount());
    }

    @Test
    void Should_Return_Response_Of_Exist_Judgement_When_Request_Exist() {
        Judgement theJudgement = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(theJudgement));
        Response actual = judgementService.get(1L);

        assertThat(actual.getJudgementId()).isSameAs(theJudgement.getJudgementId());
    }

    @Test
    void Should_Return_Response_of_Exist_Judgement_When_Request_Application_Id() {
        Judgement theJudgement = Judgement.builder()
                .judgementId(1L)
                .build();

        Application theApplication = Application.builder()
                .applicationId(1L)
                .build();

        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(theApplication));
        when(judgementRepository.findByApplicationId(1L)).thenReturn(Optional.ofNullable(theJudgement));

        Response actual = judgementService.getJudgementOfApplication(1L);

        assertThat(actual.getJudgementId()).isSameAs(theJudgement.getJudgementId());
    }

    @Test
    void Should_Return_Response_of_Updated_Judgement_When_Request_Update_Info() {
        Judgement theJudgement = Judgement.builder()
                .judgementId(1L)
                .name("Employee Kim")
                .approvalAmount(BigDecimal.valueOf(5_000_000))
                .build();
        Request request = Request.builder()
                .name("Employee Lee")
                .approvalAmount(BigDecimal.valueOf(10_000_000))
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(theJudgement));
        when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(theJudgement);
        Response actual = judgementService.update(1L, request);

        assertThat(actual.getJudgementId()).isSameAs(theJudgement.getJudgementId());
        assertThat(actual.getName()).isSameAs(request.getName());
        assertThat(actual.getApprovalAmount()).isSameAs(request.getApprovalAmount());
    }

    @Test
    void Should_Deleted_Judgement_When_Request_Delete_Exist_Judgement() {
        Judgement theJudgement = Judgement.builder()
                .judgementId(1L)
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(theJudgement));
        when(judgementRepository.save(ArgumentMatchers.any(Judgement.class))).thenReturn(theJudgement);

        judgementService.delete(1L);

        assertThat(theJudgement.getIsDeleted()).isTrue();
    }

    @Test
    void Should_Return_Update_Response_Of_Exist_Application_When_Request_Grant_Approval_Of_Judgement_Info() {
        Judgement theJudgement = Judgement.builder()
                .judgementId(1L)
                .applicationId(1L)
                .name("Employee Kim")
                .approvalAmount(BigDecimal.valueOf(5_000_000))
                .build();
        Application theApplication = Application.builder()
                .applicationId(1L)
                .approvalAmount(BigDecimal.valueOf(5_000_000))
                .build();

        when(judgementRepository.findById(1L)).thenReturn(Optional.ofNullable(theJudgement));
        when(applicationRepository.findById(1L)).thenReturn(Optional.ofNullable(theApplication));
        when(applicationRepository.save(ArgumentMatchers.any(Application.class))).thenReturn(theApplication);

        GrantAmount actual = judgementService.grant(1L);

        assertThat(actual.getApplicationId()).isSameAs(theJudgement.getApplicationId());
        assertThat(actual.getApprovalAmount()).isSameAs(theJudgement.getApprovalAmount());
    }
}
