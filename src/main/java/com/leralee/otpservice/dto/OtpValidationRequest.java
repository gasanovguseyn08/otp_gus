package com.leralee.otpservice.dto;

/**
 * @author valeriali
 * @project otp-service
 */
public record OtpValidationRequest(
        String code,
        String operationId
) {}