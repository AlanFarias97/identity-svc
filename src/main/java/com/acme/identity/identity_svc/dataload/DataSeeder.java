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
        // Roles básicos
        Role admin = roles.findByName("ROLE_ADMIN").orElseGet(() -> roles.save(Role.builder().name("ROLE_ADMIN").build()));
        Role user  = roles.findByName("ROLE_USER").orElseGet(() -> roles.save(Role.builder().name("ROLE_USER").build()));
        
        // Roles para servicios
        Role serviceRole = roles.findByName("ROLE_SERVICE").orElseGet(() -> roles.save(Role.builder().name("ROLE_SERVICE").build()));
        
        // Usuario demo
        users.findByUsername("demo").orElseGet(() -> {
            var u = User.builder()
                    .username("demo")
                    .password(encoder.encode("demo"))
                    .enabled(true)
                    .roles(Set.of(admin, user))
                    .build();
            return users.save(u);
        });
        
        // Usuario de servicio para HR
        users.findByUsername("hr-service").orElseGet(() -> {
            var u = User.builder()
                    .username("hr-service")
                    .password(encoder.encode("hr-service-secret"))
                    .fullName("HR Service User")
                    .email("hr-service@acme.com")
                    .enabled(true)
                    .roles(Set.of(serviceRole))
                    .build();
            return users.save(u);
        });
        
        // Usuario de servicio para Billing
        users.findByUsername("billing-service").orElseGet(() -> {
            var u = User.builder()
                    .username("billing-service")
                    .password(encoder.encode("billing-service-secret"))
                    .fullName("Billing Service User")
                    .email("billing-service@acme.com")
                    .enabled(true)
                    .roles(Set.of(serviceRole))
                    .build();
            return users.save(u);
        });
    }
}
