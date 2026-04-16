package com.unihub.classroom.clazz.model;

import com.unihub.classroom.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"classroom_id", "email"})
        }
        )
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID rid;

    @Column(nullable = false)
    private String email;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="classroom_id",nullable = false)
    private ClassRoom classroom;
}
