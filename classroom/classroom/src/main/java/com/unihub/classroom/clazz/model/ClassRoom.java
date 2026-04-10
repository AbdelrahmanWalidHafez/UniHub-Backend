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
public class ClassRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)
    private String code;

    @Column(nullable = false)
    private String classTitle;

    @Column(nullable = false)
    private String classSubTitle;

    @Column(nullable = false)
    private int imageNum;

    @Column(nullable = false)
    private boolean isArchived;

    @Column(nullable = false)
    private UUID universityId;

    @Column(nullable = false)
    private UUID collegeId;


}
