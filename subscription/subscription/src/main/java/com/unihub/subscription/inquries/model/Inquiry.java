package com.unihub.subscription.inquries.model;


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
public class Inquiry extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    String customerEmail;

    @Column(columnDefinition = "TEXT")
    String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

}
