package com.unihub.classroom.assginement.model;

import com.unihub.classroom.common.model.BaseEntity;
import com.unihub.classroom.material.model.Material;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Assignment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(optional = false)
    @JoinColumn(name = "material_id")
    private Material material;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private Integer points;
}
