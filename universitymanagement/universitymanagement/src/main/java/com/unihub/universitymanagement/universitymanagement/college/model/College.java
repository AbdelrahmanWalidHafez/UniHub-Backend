package com.unihub.universitymanagement.universitymanagement.college.model;

import com.unihub.universitymanagement.universitymanagement.common.model.BaseEntity;
import com.unihub.universitymanagement.universitymanagement.university.model.University;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class College extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String collegeName;

    @Column(nullable = false)
    private String campus;
    // MANY colleges belong to ONE university
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "university_id",updatable = false) // foreign key column
    private University university;
}