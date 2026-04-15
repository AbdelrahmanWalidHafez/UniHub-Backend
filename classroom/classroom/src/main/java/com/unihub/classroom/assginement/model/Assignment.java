package com.unihub.classroom.assginement.model;

import com.unihub.classroom.common.model.BaseEntity;
import com.unihub.classroom.material.model.Material;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_assignment_material_id", columnList = "material_id")
        }
)
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


    @Builder.Default
    @OneToMany(mappedBy = "assignment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Submission> submissions=new ArrayList<>();


    public void addSubmission(Submission submission) {
        submissions.add(submission);
        submission.setAssignment(this);
    }

    public void removeSubmission(Submission submission) {
        submissions.remove(submission);
        submission.setAssignment(null);
    }
}
