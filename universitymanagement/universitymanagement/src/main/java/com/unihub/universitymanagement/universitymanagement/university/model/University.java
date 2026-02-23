package com.unihub.universitymanagement.universitymanagement.university.model;

import com.unihub.universitymanagement.universitymanagement.college.model.College;
import com.unihub.universitymanagement.universitymanagement.common.model.BaseEntity;
import com.unihub.universitymanagement.universitymanagement.subscription.model.UniversitySubscriptionPlan;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class University extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uniId;

    @Column(unique = true,nullable = false)
    private String universityName;

    @Column(unique = true,nullable = false)
    private String universityEmail;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String city;

    @Column(unique = true,nullable = false)
    private String contactNumber;

    @Column(nullable = false,unique = true)
    private String universityWebsiteUrl;

    @Column(nullable = false,unique = true)
    private String universityDomain;

    @Column(nullable = false,unique = true)
    private String universityLogoKey;

    @Column(nullable = false,unique = true)
    private String accreditationKey;

    @OneToOne(mappedBy = "university", fetch = FetchType.EAGER,cascade = CascadeType.ALL,orphanRemoval = true)
    private UniversitySubscriptionPlan subscriptionPlan;

    @OneToMany(
            mappedBy = "university",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<College> colleges;

}
