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
@Table(
        indexes = {
                @Index(name = "idx_comment_material_id", columnList = "material_id"),
                @Index(name = "idx_comment_created_by", columnList = "created_by"),
        }
)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cid;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean edited;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "material_id", nullable = false)
    private Material material;

}