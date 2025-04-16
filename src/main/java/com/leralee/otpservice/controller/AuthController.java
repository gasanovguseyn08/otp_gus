package com.leralee.otpservice.controller;

import com.leralee.otpservice.dto.LoginRequest;
import com.leralee.otpservice.dto.RegisterRequest;
import com.leralee.otpservice.dto.UserResponse;
import com.leralee.otpservice.security.CustomUserDetails;
import com.leralee.otpservice.security.JwtService;
import com.leralee.otpservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author valeriali
 * @project otp-service
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        return userService.register(
                        request.login(),
                        request.email(),
                        request.password(),
                        request.telegramChatId(),
                        request.phoneNumber())
                .map(user -> ResponseEntity.ok(new UserResponse(user)))
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().body("Пользователь уже существует или админ уже есть"));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.login(request.login(), request.password())
                .map(user -> jwtService.generateToken(new CustomUserDetails(user)))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body("Неверный логин или пароль"));
    }
}
