package com.unihub.subscription.inquries.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    String customerEmail;

    @Column(columnDefinition = "TEXT")
    String subject;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    LocalDateTime createdAt;

}
