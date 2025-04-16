package com.leralee.otpservice.dto;

/**
 * @author valeriali
 * @project otp-service
 */
public record GenerateOtpRequest(
        String operationId,
        Channel channel
) {
    public enum Channel {
        EMAIL, TELEGRAM, SMS, FILE
    }
}