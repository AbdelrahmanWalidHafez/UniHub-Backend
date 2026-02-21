package com.unihub.universitymanagement.universitymanagement.subscription.model;

import com.unihub.universitymanagement.universitymanagement.university.model.University;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UniversitySubscriptionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID pid;

    @Column(updatable = false)
    private LocalDate startDate;

    @Column(updatable = false)
    private LocalDate endDate;

    @OneToOne()
    @JoinColumn(name = "university_id")
    private University university;

}
