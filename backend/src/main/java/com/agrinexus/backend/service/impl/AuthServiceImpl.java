package com.agrinexus.backend.service.impl;

import com.agrinexus.backend.dto.request.LoginRequestDTO;
import com.agrinexus.backend.dto.request.RefreshTokenRequestDTO;
import com.agrinexus.backend.dto.request.RegisterRequestDTO;
import com.agrinexus.backend.dto.response.AuthResponseDTO;
import com.agrinexus.backend.dto.response.UserResponseDTO;
import com.agrinexus.backend.model.Role;
import com.agrinexus.backend.model.User;
import com.agrinexus.backend.repository.UserRepository;
import com.agrinexus.backend.security.JwtUtil;
import com.agrinexus.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserResponseDTO register(RegisterRequestDTO requestDTO) {

        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("Registration attempt with existing email: {}", requestDTO.getEmail());
            throw new RuntimeException("Email already registered"); // placeholder — Module 5 mein proper exception
        }

        User user = User.builder()
                .name(requestDTO.getName())
                .email(requestDTO.getEmail())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .phoneNumber(requestDTO.getPhoneNumber())
                .role(Role.FARMER)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        User savedUser = userRepository.save(user);
        log.info("New user registered: {}", savedUser.getEmail());

        return mapToResponseDTO(savedUser);
    }

    @Override
    public AuthResponseDTO login(LoginRequestDTO requestDTO) {

        User user = userRepository.findByEmail(requestDTO.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials")); // placeholder — Module 5 mein 401 exception

        if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
            log.warn("Failed login attempt for email: {}", requestDTO.getEmail());
            throw new RuntimeException("Invalid credentials");  // SAME message as above — enumeration prevention
        }

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user);

        user.setRefreshToken(refreshToken);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        log.info("User logged in: {}", user.getEmail());

        return AuthResponseDTO.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private UserResponseDTO mapToResponseDTO(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .phoneNumber(user.getPhoneNumber())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Override
    public AuthResponseDTO refresh(RefreshTokenRequestDTO requestDTO) {

        String refreshToken = requestDTO.getRefreshToken();

        if (!jwtUtil.isTokenValid(refreshToken)) {
            throw new RuntimeException("Invalid or expired refresh token"); // placeholder — Module 5 mein 401
        }

        String email = jwtUtil.extractEmail(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Critical DB-backed check — yeh hi logout ko meaningful banata hai
        if (!refreshToken.equals(user.getRefreshToken())) {
            log.warn("Refresh token mismatch/reuse attempt for user: {}", email);
            throw new RuntimeException("Invalid refresh token");
        }

        String newAccessToken = jwtUtil.generateAccessToken(user);

        log.info("Access token refreshed for user: {}", email);

        return AuthResponseDTO.builder()
                .accessToken(newAccessToken)
                .refreshToken(refreshToken)   // SAME refresh token returned — no rotation in V1
                .build();
    }

    @Override
    public void logout(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setRefreshToken(null);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        log.info("User logged out: {}", email);
    }
}