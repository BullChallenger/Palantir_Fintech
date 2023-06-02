package com.palantir.fintech.service;

import com.palantir.fintech.domain.Counsel;
import com.palantir.fintech.dto.CounselDTO.Response;
import com.palantir.fintech.dto.CounselDTO.Request;
import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {

    private final ModelMapper modelMapper;

    private final CounselRepository counselRepository;

    @Override
    public Response create(Request request) {
        Counsel counsel = modelMapper.map(request, Counsel.class);
        counsel.setAppliedAt(LocalDateTime.now());

        Counsel createdCounsel = counselRepository.save(counsel);

        return modelMapper.map(createdCounsel, Response.class);
    }

    @Override
    public Response get(Long counselId) {
        Counsel theCounsel = counselRepository.findById(counselId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR));
        return modelMapper.map(theCounsel, Response.class);
    }

    @Override
    public Response update(Long counselId, Request request) {
        Counsel theCounsel = counselRepository.findById(counselId).orElseThrow(
                () -> new BaseException(ResultType.SYSTEM_ERROR)
        );

        theCounsel.setName(request.getName());
        theCounsel.setCellPhone(request.getCellPhone());
        theCounsel.setEmail(request.getEmail());
        theCounsel.setMemo(request.getMemo());
        theCounsel.setAddress(request.getAddress());
        theCounsel.setAddressDetail(request.getAddressDetail());
        theCounsel.setZipCode(request.getZipCode());

        counselRepository.save(theCounsel);

        return modelMapper.map(theCounsel, Response.class);
    }

    @Override
    public void delete(Long counselId) {
        Counsel theCounsel = counselRepository.findById(counselId).orElseThrow(() ->
            new BaseException(ResultType.SYSTEM_ERROR)
        );
        theCounsel.setIsDeleted(true);
        counselRepository.save(theCounsel);
    }
}
