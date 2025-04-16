package com.leralee.otpservice.dto;

import com.leralee.otpservice.model.User;

/**
 * @author valeriali
 * @project otp-service
 */
public record UserResponse(
        String login,
        String email,
        String phoneNumber
) {
    public UserResponse(User user) {
        this(user.getLogin(), user.getEmail(), user.getPhoneNumber());
    }
}
