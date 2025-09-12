package com.acme.identity.identity_svc.dataload;

import com.acme.identity.identity_svc.user.Role;
import com.acme.identity.identity_svc.user.RoleRepository;
import com.acme.identity.identity_svc.user.User;
import com.acme.identity.identity_svc.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roles;
    private final UserRepository users;
    private final PasswordEncoder encoder;

    @Override
    public void run(String... args) {
        Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));
        Role user  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));

        users.findByUsername("demo").orElseGet(() -> {
            var u = User.builder()
                    .username("demo")
                    .password(encoder.encode("demo"))
                    .enabled(true)
                    .roles(Set.of(admin, user))
                    .build();
            return users.save(u);
        });
    }
}
