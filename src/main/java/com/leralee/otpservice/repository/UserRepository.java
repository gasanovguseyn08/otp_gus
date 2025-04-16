package com.leralee.otpservice.repository;

import com.leralee.otpservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author valeriali
 * @project otp-service
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);

    boolean existsByRole(User.Role role);

    boolean existsByLogin(String login);

    boolean existsByEmail(String email);

    List<User> findAllByRole(User.Role role);
}
