package com.unihub.authorization.security.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID rid;

    @Column(unique = true, nullable = false, updatable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
