package com.alibou.batch.repo;

import com.alibou.batch.entity.CustIndicator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustIndicatorRepository extends JpaRepository<CustIndicator, String> {
    Optional<CustIndicator> findByRelId(String relId);
}