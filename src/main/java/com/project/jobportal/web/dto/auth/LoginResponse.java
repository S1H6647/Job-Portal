package com.project.jobportal.web.dto.auth;

import com.project.jobportal.domain.Role;

import java.util.UUID;

public record LoginResponse(
        UUID id,
        String email,
        Role role,
        String token
) {
}
