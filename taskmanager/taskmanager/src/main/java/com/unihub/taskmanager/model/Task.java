package com.unihub.taskmanager.model;

import com.unihub.taskmanager.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;
//TOREMOVE finshedAt StartedAT
@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID )
    private UUID id;

    @Column(nullable = false,columnDefinition ="TEXT")
    private String description;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    @Enumerated
    private Status status;

    @Column(nullable = false)
    @Enumerated
    private Priority priority;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    @Column(insertable = false)
    private LocalDateTime startedAt;

    @Column(insertable = false)
    private LocalDateTime finishedAt;

}
