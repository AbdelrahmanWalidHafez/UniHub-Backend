package com.unihub.classroom.material.model;

import com.unihub.classroom.clazz.model.ClassRoom;
import com.unihub.classroom.comment.model.Comment;
import com.unihub.classroom.common.model.BaseEntity;
import com.unihub.classroom.material.model.enums.MaterialType;
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
public class Material extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mid;

    @Column(nullable = false)
    private String headLine;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MaterialType materialType;

    @ElementCollection
    @Column(name = "url")
    @Builder.Default
    @CollectionTable(name = "material_urls", joinColumns = @JoinColumn(name = "material_id"))
    private List<String> materialUrls=new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="classroom_id",nullable = false)
    private ClassRoom classroom;


    @Builder.Default
    @OneToMany(mappedBy = "material", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments=new ArrayList<>();

    public void addComment(Comment comment) {
        comments.add(comment);
        comment.setMaterial(this);
    }

    public void removeComment(Comment comment) {
        comments.remove(comment);
        comment.setMaterial(null);
    }


}
