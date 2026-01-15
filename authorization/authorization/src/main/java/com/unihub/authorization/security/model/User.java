package com.unihub.authorization.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uid;

    @Column(unique = true,nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    boolean isAccountNonLocked;

    @ManyToOne
    @JoinColumn(name = "rid",nullable = false)
    private Role role;
}
