package com.leralee.otpservice.controller;

import com.leralee.otpservice.dto.UserResponse;
import com.leralee.otpservice.model.OtpConfig;
import com.leralee.otpservice.model.User;
import com.leralee.otpservice.repository.OtpCodeRepository;
import com.leralee.otpservice.repository.OtpConfigRepository;
import com.leralee.otpservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OtpConfigRepository otpConfigRepository;
    private final OtpCodeRepository otpCodeRepository;
    private final UserRepository userRepository;

    @GetMapping("/config")
    public ResponseEntity<OtpConfig> getCurrentConfig() {
        return otpConfigRepository.findTopByOrderById()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/config")
    public ResponseEntity<OtpConfig> updateConfig(@RequestBody OtpConfig updated) {
        OtpConfig config = otpConfigRepository.findTopByOrderById()
                .orElse(new OtpConfig());
        config.setCodeLength(updated.getCodeLength());
        config.setExpirationMinutes(updated.getExpirationMinutes());

        return ResponseEntity.ok(otpConfigRepository.save(config));
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserResponse>> getAllNonAdminUsers() {
        List<User> users = userRepository.findAllByRole(User.Role.USER);

        List<UserResponse> response = users.stream()
                .map(UserResponse::new)
                .toList();

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/users/{login}")
    public ResponseEntity<?> deleteUser(@PathVariable String login) {
        Optional<User> userOptional = userRepository.findByLogin(login);

        if (userOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        otpCodeRepository.deleteAllByLogin(login);

        userRepository.delete(userOptional.get());

        return ResponseEntity.ok("Пользователь и его OTP-коды удалены");
    }
}
