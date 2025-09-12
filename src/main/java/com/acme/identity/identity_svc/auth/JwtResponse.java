package com.acme.identity.identity_svc.auth;

public record JwtResponse(String token, long expiresAtEpochMillis) {}
