package com.palantir.fintech.service;

import com.palantir.fintech.exception.BaseException;
import com.palantir.fintech.exception.ResultType;
import com.palantir.fintech.repository.ApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {

    private final ApplicationRepository applicationRepository;

    @Value("${spring.servlet.multipart.location}")
    private String uploadPath;

    @Override
    public void save(Long applicationId, MultipartFile file) {
        if (isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path directoryPath = Path.of(applicationPath);
            if (!Files.exists(directoryPath)) {
                Files.createDirectory(directoryPath);
            }

            Files.copy(file.getInputStream(),
                    directoryPath.resolve(file.getOriginalFilename()),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }

    }

    @Override
    public Resource load(Long applicationId, String fileName) {
        if (isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            Path file = Paths.get(applicationPath).resolve(fileName);
            Resource resource = new UrlResource(file.toUri());

            if (resource.isReadable() || resource.exists()) {
                return resource;
            } else {
                throw new BaseException(ResultType.NOT_EXIST);
            }
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public Stream<Path> loadAll(Long applicationId) {
        if (isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        try {
            String applicationPath = uploadPath.concat("/" + applicationId);
            return Files.walk(Paths.get(applicationPath), 1).filter(path -> !path.equals(Paths.get(applicationPath)));
        } catch (Exception e) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
    }

    @Override
    public void deleteAll(Long applicationId) {
        if (isPresentApplication(applicationId)) {
            throw new BaseException(ResultType.SYSTEM_ERROR);
        }
        String applicationPath = uploadPath.concat("/" + applicationId);
        FileSystemUtils.deleteRecursively(Paths.get(applicationPath).toFile());
    }

    private boolean isPresentApplication(Long applicationId) {
        return applicationRepository.findById(applicationId).isPresent();
    }
}
