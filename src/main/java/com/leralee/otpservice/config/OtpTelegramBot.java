package com.leralee.otpservice.config;

import com.leralee.otpservice.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * @author valeriali
 * @project otp-service
 */
@Component
public class OtpTelegramBot extends TelegramLongPollingBot {

    private final UserService userService;

    private final Dotenv dotenv = Dotenv.load();

    private final String botToken = dotenv.get("TELEGRAM_BOT_TOKEN");

    @Value("${telegram.bot.username}")
    private String botUsername;

    public OtpTelegramBot(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            String fromLogin = update.getMessage().getFrom().getUserName();

            if (message.startsWith("/start")) {
                userService.bindTelegramChatId(fromLogin, String.valueOf(chatId));

                String response = "Готово";
                sendMessage(chatId, response);
            }
        }
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void sendMessage(Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка при отправке в Telegram", e);
        }
    }
}
