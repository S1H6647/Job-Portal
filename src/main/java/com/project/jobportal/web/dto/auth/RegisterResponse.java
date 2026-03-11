package com.project.jobportal.web.dto.auth;

import com.project.jobportal.domain.Role;
import com.project.jobportal.domain.User;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String email,
        Role role,
        Instant createdAt
) {

    public static RegisterResponse from(User user) {
        return new RegisterResponse(
                user.getId(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
