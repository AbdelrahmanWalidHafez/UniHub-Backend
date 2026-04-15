package com.unihub.classroom.clazz.model;

import com.unihub.classroom.common.model.BaseEntity;
import com.unihub.classroom.material.model.Material;
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
        name = "class_room",
        indexes = {

                @Index(
                        name = "idx_classroom_tenant_id", columnList = "id, college_id, university_id"),
                @Index(
                        name = "idx_classroom_code", columnList = "code"),
        }
)
public class ClassRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(unique = true,nullable = false)
    private String code;

    @Column(nullable = false)
    private String classTitle;

    @Column(nullable = false)
    private String classSubTitle;

    @Column(nullable = false)
    private int imageNum;

    @Column(nullable = false)
    private boolean archived;

    @Column(nullable = false)
    private UUID universityId;

    @Column(nullable = false)
    private UUID collegeId;

    @Builder.Default
    @OneToMany(
            mappedBy = "classroom",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Member> members=new ArrayList<>();

    @Builder.Default
    @OneToMany(
            mappedBy = "classroom",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Material> materials=new ArrayList<>();
    public void addMember(Member member) {
        members.add(member);
        member.setClassroom(this);
    }

    public void removeMember(Member member) {
        members.remove(member);
        member.setClassroom(null);
    }

    public void addMaterial(Material material) {
        materials.add(material);
        material.setClassroom(this);
    }

    public void removeMaterial(Material material) {
        materials.remove(material);
        material.setClassroom(null);
    }
}
