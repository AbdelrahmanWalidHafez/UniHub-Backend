package com.unihub.announcement.comment.model;

import com.unihub.announcement.common.model.BaseEntity;
import com.unihub.announcement.post.model.Post;
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
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cid;


    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private boolean edited;

    @Column(nullable = false)
    private long repliesCounts;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL,orphanRemoval = true,fetch = FetchType.LAZY)
    private List<Comment> replies = new ArrayList<>();

}
