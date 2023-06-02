package com.palantir.fintech.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.*;

import com.palantir.fintech.domain.Counsel;
import com.palantir.fintech.dto.CounselDTO.Response;
import com.palantir.fintech.dto.CounselDTO.Request;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.CounselRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CounselServiceTest {

    @InjectMocks
    CounselServiceImpl counselService;

    @Mock
    private CounselRepository counselRepository;

    @Spy
    private ModelMapper modelMapper;

    @Test
    void Should_Return_Response_Of_New_Counsel_Entity() {
        Counsel theCounsel = Counsel.builder()
                                    .name("Member Kim")
                                    .cellPhone("010-1111-2222")
                                    .email("test@test.com")
                                    .memo("대출받고 싶습니다. 연락주세요.")
                                    .zipCode("12345")
                                    .address("부산광역시 금정구 남산동")
                                    .addressDetail("어쩌구 아파트 000동 000호")
                                .build();
        Request request = Request.builder()
                                    .name("Member Kim")
                                    .cellPhone("010-1111-2222")
                                    .email("test@test.com")
                                    .memo("대출받고 싶습니다. 연락주세요.")
                                    .zipCode("12345")
                                    .address("부산광역시 금정구 남산동")
                                    .addressDetail("어쩌구 아파트 000동 000호")
                                .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(theCounsel);

        Response actual = counselService.create(request);
        assertThat(actual.getName()).isSameAs(theCounsel.getName());
    }

    @Test
    void Should_Return_Response_Of_Exist_Counsel_Entity() {
        Long counselId = 1L;
        Counsel theCounsel = Counsel.builder()
                                    .counselId(counselId)
                                    .build();

        when(counselRepository.findById(counselId)).thenReturn(Optional.ofNullable(theCounsel));

        Response actual = counselService.get(counselId);
        assertThat(counselId).isEqualTo(actual.getCounselId());
    }

    @Test
    void Should_Throw_Exception_Request_Not_Exist_CounselId() {
        Long counselId = 1L;
        when(counselRepository.findById(counselId)).thenThrow(new BaseException(ResultType.SYSTEM_ERROR));

        assertThrows(BaseException.class, () -> counselService.get(counselId));
    }

    @Test
    void Should_Return_Updated_Counsel_Entity() {
        Long counselId = 1L;
        Counsel theCounsel = Counsel.builder()
                                    .counselId(counselId)
                                    .name("Member Kim")
                                    .build();

        Request request = Request.builder()
                                    .name("Member Kang")
                                    .build();

        when(counselRepository.save(ArgumentMatchers.any(Counsel.class))).thenReturn(theCounsel);
        when(counselRepository.findById(counselId)).thenReturn(Optional.ofNullable(theCounsel));

        Response actual = counselService.update(counselId, request);

        assertThat(actual.getCounselId()).isEqualTo(theCounsel.getCounselId());
        assertThat(actual.getName()).isSameAs(request.getName());
    }

    @Test
    void Should_Return_Nothing_When_Counsel_Deleted() {
        Long counselId = 1L;
        Counsel theCounsel = Counsel.builder()
                                    .counselId(counselId)
                                    .build();

        when(counselRepository.findById(counselId)).thenReturn(Optional.ofNullable(theCounsel));
        when(counselRepository.save(theCounsel)).thenReturn(theCounsel);

        counselService.delete(counselId);

        assertThat(theCounsel.getIsDeleted()).isTrue();
    }
}
