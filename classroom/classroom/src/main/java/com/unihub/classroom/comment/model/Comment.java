package com.unihub.classroom.comment.model;


import com.unihub.classroom.common.model.BaseEntity;
import com.unihub.classroom.material.model.Material;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cid;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean edited;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

}