package com.akaci.twotterbackend.persistence.entity;

import com.akaci.twotterbackend.persistence.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_logged_in")
    private LocalDateTime lastLoggedInAt;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role")
    private RoleJpaEntity role;

    public AccountJpaEntity(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.createdAt = LocalDateTime.now();
        this.role = new RoleJpaEntity(role);
    }



}
