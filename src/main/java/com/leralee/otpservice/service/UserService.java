package com.leralee.otpservice.service;

import com.leralee.otpservice.model.User;
import com.leralee.otpservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<User> register(String login, String email, String password, String telegramChatId, String phoneNumber) {

        if (userRepository.existsByLogin(login) || userRepository.existsByEmail(email)) {
            return Optional.empty();
        }

        String encodedPassword = passwordEncoder.encode(password);

        User user = new User();
        user.setLogin(login);
        user.setEmail(email);
        user.setTelegramChatId(telegramChatId);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(encodedPassword);
        user.setRole(User.Role.USER);

        return Optional.of(userRepository.save(user));
    }

    public Optional<User> login(String login, String password) {
        return userRepository.findByLogin(login)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    public void bindTelegramChatId(String telegramUsername, String chatId) {
        userRepository.findByLogin(telegramUsername).ifPresent(user -> {
            user.setTelegramChatId(chatId);
            userRepository.save(user);
        });
    }

    public void updatePhoneNumber(String login, String phoneNumber) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден"));

        user.setPhoneNumber(phoneNumber);
        userRepository.save(user);
    }

}
