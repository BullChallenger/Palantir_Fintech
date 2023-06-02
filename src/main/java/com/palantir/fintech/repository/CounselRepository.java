package com.palantir.fintech.repository;

import com.palantir.fintech.domain.Counsel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CounselRepository extends JpaRepository<Counsel, Long> {
}
