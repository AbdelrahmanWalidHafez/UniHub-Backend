package com.unihub.classroom.assginement.model;

import com.unihub.classroom.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_submission_assignment_id", columnList = "assignment_id"),
                @Index(name = "idx_submission_created_by", columnList = "created_by"),
                @Index(name = "idx_submission_assignment_created_by", columnList = "assignment_id, created_by")
        }
)
public class Submission extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID sid;

    @Column(nullable = false)
    private boolean edited;

    @Column(nullable = false)
    private Integer grade;

    @ElementCollection
    @Column(name = "url")
    @Builder.Default
    @CollectionTable(name = "submission_urls", joinColumns = @JoinColumn(name = "submission_id"))
    private List<String> submissionUrls=new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "assignment_id", nullable = false)
    private Assignment assignment;

    private Integer gradePoints;
}
