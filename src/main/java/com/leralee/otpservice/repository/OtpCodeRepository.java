package com.leralee.otpservice.repository;

import com.leralee.otpservice.model.OtpCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */
public interface OtpCodeRepository extends JpaRepository<OtpCode, Long> {

    Optional<OtpCode> findByCodeAndLoginAndOperationId(String code, String login, String operationId);

    void deleteAllByLogin(String login);
}
