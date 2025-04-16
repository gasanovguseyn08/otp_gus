package com.leralee.otpservice.dto;

/**
 * @author valeriali
 * @project otp-service
 */
public record LoginRequest(
        String login,
        String password
) {
}
