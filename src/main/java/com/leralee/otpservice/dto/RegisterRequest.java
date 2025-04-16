package com.leralee.otpservice.dto;

/**
 * @author valeriali
 * @project otp-service
 */
public record RegisterRequest(
        String login,
        String email,
        String telegramChatId,
        String phoneNumber,
        String password
) {
}