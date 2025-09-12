package com.acme.identity.identity_svc.auth;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank String password,
        String fullName,
        String email
) {}

