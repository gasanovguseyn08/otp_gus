package com.leralee.otpservice.repository;

import com.leralee.otpservice.model.OtpConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */
public interface OtpConfigRepository extends JpaRepository<OtpConfig, Long> {
    Optional<OtpConfig> findTopByOrderById();
}
