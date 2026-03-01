package com.unihub.auth.security.model;

import com.unihub.auth.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Setter
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users", indexes = {
        @Index(name = "idx_user_email", columnList = "email"),
        @Index(name = "idx_user_first_name", columnList = "first_name"),
        @Index(name = "idx_user_last_name", columnList = "last_name")
})
public class User extends BaseEntity {

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

    @Column(nullable = true)
    private String password;

    @Version
    private long version;

    @Column(nullable = false)
    boolean isAccountNonLocked;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "rid",nullable = false)
    private Role role;

    @JoinColumn(updatable = false)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private UniversityMetadata universityMetadata;
}
