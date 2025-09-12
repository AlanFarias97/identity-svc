package com.acme.identity.identity_svc.auth;


import com.acme.identity.identity_svc.security.*;
import com.acme.identity.identity_svc.user.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    @Value("${security.jwt.expiration-seconds}") long expSeconds;

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> register(@RequestBody @Valid RegisterRequest req) {
        if (users.existsByUsername(req.username())) {
            return ResponseEntity.status(409).body("username already exists");
        }
        var roleUser = roles.findByName("ROLE_USER")
                .orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));

        var u = User.builder()
                .username(req.username())
                .password(encoder.encode(req.password()))
                .fullName(req.fullName())
                .email(req.email())
                .enabled(true)
                .build();
        u.getRoles().add(roleUser);
        users.save(u);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@RequestBody @Valid LoginRequest req) {
        var u = users.findByUsername(req.username())
                .orElseThrow(() -> new RuntimeException("Bad credentials"));

        if (!encoder.matches(req.password(), u.getPassword()))
            throw new RuntimeException("Bad credentials");

        var token = jwt.generate(u.getUsername());
        var expiresAt = Instant.now().plusSeconds(expSeconds).toEpochMilli();
        return ResponseEntity.ok(new JwtResponse(token, expiresAt));
    }
}
