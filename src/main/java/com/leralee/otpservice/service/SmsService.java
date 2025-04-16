package com.leralee.otpservice.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author valeriali
 * @project otp-service
 */
@Service
@RequiredArgsConstructor
public class SmsService {

    private final Dotenv dotenv = Dotenv.load();

    private final String accountSid = dotenv.get("TWILIO_ACCOUNT_SID");
    private final String authToken = dotenv.get("TWILIO_AUTH_TOKEN");
    private final String fromPhoneNumber = dotenv.get("TWILIO_PHONE_NUMBER");

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
    }

    public void sendSms(String toPhoneNumber, String code, String operationId) {

        String messageText = "OTP-код для операции " + operationId + ": " + code;

        Message.creator(
                new PhoneNumber(toPhoneNumber),
                new PhoneNumber(fromPhoneNumber),
                messageText
        ).create();
    }
}
