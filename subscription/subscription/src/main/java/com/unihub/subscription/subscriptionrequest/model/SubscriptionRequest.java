package com.unihub.subscription.subscriptionrequest.model;

import com.unihub.subscription.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID rid;

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

    @Column(nullable = false)
    private String universityWebsiteUrl;

    @Column(nullable = false)
    private String universityDomain;

    @Column(nullable = false)
    private String universityLogoKey;

    @Column(nullable = false)
    private String accreditationKey;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;
}
