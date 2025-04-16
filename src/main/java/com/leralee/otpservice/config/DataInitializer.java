package com.leralee.otpservice.config;

import com.leralee.otpservice.model.OtpConfig;
import com.leralee.otpservice.repository.OtpConfigRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author valeriali
 * @project otp-service
 */
@Component
@RequiredArgsConstructor
public class DataInitializer {
    private final OtpConfigRepository otpConfigRepository;

    @PostConstruct
    public void init() {
        if (otpConfigRepository.findTopByOrderById().isEmpty()) {
            otpConfigRepository.save(new OtpConfig(null, 6, 5));
        }
    }
}
