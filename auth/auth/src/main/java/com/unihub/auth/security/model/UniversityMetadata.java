package com.unihub.auth.security.model;

import com.unihub.auth.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_tid", columnList = "tid")
        }
)
public class UniversityMetadata extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID uid;

    @Column(nullable = false)
    UUID tid;

    UUID cid;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;
}
