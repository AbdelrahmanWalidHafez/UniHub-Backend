package com.unihub.auth.security.model;

import com.unihub.auth.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID rid;

    @Column(unique = true, nullable = false, updatable = false)
    private String name;

    @OneToMany(mappedBy = "role")
    private List<User> users;
}
