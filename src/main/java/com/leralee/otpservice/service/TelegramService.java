package com.leralee.otpservice.service;

import com.leralee.otpservice.config.OtpTelegramBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * @author valeriali
 * @project otp-service
 */
@Service
@RequiredArgsConstructor
public class TelegramService {

    private final OtpTelegramBot telegramBot;

    public void sendOtp(String chatId, String code, String operationId) {
        telegramBot.sendMessage(Long.valueOf(chatId), "OTP-код для операции " + operationId + ": " + code);
    }
}
