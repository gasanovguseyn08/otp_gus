package com.leralee.otpservice.controller;

import com.leralee.otpservice.dto.GenerateOtpRequest;
import com.leralee.otpservice.dto.OtpValidationRequest;
import com.leralee.otpservice.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author valeriali
 * @project otp-service
 */
@RestController
@RequestMapping("/otp")
@RequiredArgsConstructor
public class OtpController {
    private final OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<?> generate(@AuthenticationPrincipal UserDetails user,
                                      @RequestBody GenerateOtpRequest request) {
        if (request.operationId() == null || request.operationId().isBlank()) {
            return ResponseEntity.badRequest().body("operationId обязателен");
        }
        if (request.channel() == null) {
            return ResponseEntity.badRequest().body("Необходимо указать канал отправки OTP");
        }

        String code = otpService.generateOtp(user.getUsername(), request.operationId(), request.channel());
        return ResponseEntity.ok(Map.of(
                "otpCode", code
        ));
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validate(@AuthenticationPrincipal UserDetails user,
                                      @RequestBody OtpValidationRequest request) {
        boolean valid = otpService.validateOtp(user.getUsername(), request.code(), request.operationId());
        if (valid) {
            return ResponseEntity.ok(Map.of("message", "Код подтверждён"));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Неверный или просроченный код"));
        }
    }
}
