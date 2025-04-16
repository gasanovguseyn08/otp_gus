package com.leralee.otpservice.service;

import com.leralee.otpservice.dto.GenerateOtpRequest;
import com.leralee.otpservice.model.OtpCode;
import com.leralee.otpservice.model.OtpConfig;
import com.leralee.otpservice.model.User;
import com.leralee.otpservice.repository.OtpCodeRepository;
import com.leralee.otpservice.repository.OtpConfigRepository;
import com.leralee.otpservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpCodeRepository otpCodeRepository;
    private final OtpConfigRepository otpConfigRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final TelegramService telegramService;
    private final SmsService smsService;

    private final SecureRandom random = new SecureRandom();

    public String generateOtp(String login, String operationId, GenerateOtpRequest.Channel channel) {
        OtpConfig config = otpConfigRepository.findTopByOrderById()
                .orElseThrow(() -> new RuntimeException("OTP конфигурация не найдена"));

        int codeLength = config.getCodeLength();
        long expirationMinutes = config.getExpirationMinutes();

        String code = generateNumericCode(codeLength);

        OtpCode otp = new OtpCode();
        otp.setCode(code);
        otp.setLogin(login);
        otp.setOperationId(operationId);
        otp.setStatus(OtpCode.Status.ACTIVE);
        otp.setCreatedAt(LocalDateTime.now());
        otp.setExpiresAt(LocalDateTime.now().plusMinutes(expirationMinutes));

        otpCodeRepository.save(otp);

        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        switch (channel) {
            case EMAIL -> emailService.sendOtpEmail(user.getEmail(), code, operationId);
            case TELEGRAM -> telegramService.sendOtp(user.getTelegramChatId(), code, operationId);
            case SMS -> {
                if (user.getPhoneNumber() == null || user.getPhoneNumber().isBlank()) {
                    throw new IllegalStateException("У пользователя не указан номер телефона");
                }
                smsService.sendSms(user.getPhoneNumber(), code, operationId);
            }
            case FILE -> saveOtpToFile(code, operationId);
        }

        return code;
    }

    private String generateNumericCode(int length) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    public boolean validateOtp(String login, String code, String operationId) {
        Optional<OtpCode> otpOptional = otpCodeRepository
                .findByCodeAndLoginAndOperationId(code, login, operationId);

        if (otpOptional.isEmpty()) return false;

        OtpCode otp = otpOptional.get();

        if (otp.getStatus() != OtpCode.Status.ACTIVE) return false;
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpCode.Status.EXPIRED);
            otpCodeRepository.save(otp);
            return false;
        }

        otp.setStatus(OtpCode.Status.USED);
        otpCodeRepository.save(otp);
        return true;
    }

    private void saveOtpToFile(String code, String operationId) {
        try {
            String filename = "otp_" + operationId + ".txt";
            String content = "OTP-код: " + code;

            Files.writeString(Path.of(filename), content);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось сохранить OTP в файл", e);
        }
    }
}
