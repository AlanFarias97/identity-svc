package com.acme.identity.identity_svc.user;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "roles")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 40)
    private String name; // "ROLE_USER", "ROLE_ADMIN"
}
