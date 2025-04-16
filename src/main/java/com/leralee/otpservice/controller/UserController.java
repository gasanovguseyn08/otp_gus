package com.leralee.otpservice.controller;

import com.leralee.otpservice.dto.PhoneNumberUpdateRequest;
import com.leralee.otpservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author valeriali
 * @project otp-service
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PatchMapping("/add-phone")
    public ResponseEntity<?> addPhone(@AuthenticationPrincipal UserDetails user,
                                      @RequestBody PhoneNumberUpdateRequest request) {
        if (request.phoneNumber() == null || request.phoneNumber().isBlank()) {
            return ResponseEntity.badRequest().body("Номер телефона обязателен");
        }

        userService.updatePhoneNumber(user.getUsername(), request.phoneNumber());
        return ResponseEntity.ok("Номер телефона сохранён");
    }

}
