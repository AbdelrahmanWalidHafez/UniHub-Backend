package com.unihub.subscription.stripe.model;

import com.unihub.subscription.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;



@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID sid;

    @Column(unique = true,nullable = false)
    private String planName;

    @Column(nullable = false,columnDefinition = "TEXT")
    private String planDescription;

    @Column(nullable = false)
    private String billingCycle;

    @Column(nullable = false)
    private Long price;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private int maxUserAmount;
}
