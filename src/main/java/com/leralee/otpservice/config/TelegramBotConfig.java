package com.leralee.otpservice.config;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

/**
 * @author valeriali
 * @project otp-service
 */
@Configuration
@RequiredArgsConstructor
public class TelegramBotConfig {
    private final OtpTelegramBot otpTelegramBot;

    @PostConstruct
    public void init() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            if (!otpTelegramBot.getBotToken().isEmpty()) {
                botsApi.registerBot(otpTelegramBot);
            }
        } catch (TelegramApiException e) {
            if (e.getMessage().contains("terminated by other getUpdates request")) {
                System.err.println("Бот уже запущен в другом экземпляре.");
            } else {
                throw new RuntimeException("Ошибка при регистрации Telegram бота", e);
            }
        }
    }
}
