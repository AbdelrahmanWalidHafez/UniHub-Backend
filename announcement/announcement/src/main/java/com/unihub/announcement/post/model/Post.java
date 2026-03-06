package com.unihub.announcement.post.model;


import com.unihub.announcement.common.model.BaseEntity;
import com.unihub.announcement.like.model.PostLike;
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
@Table(
        indexes = {
                @Index(name = "idx_post_cid_status", columnList = "cid,status"),
                @Index(name = "idx_post_cid_createdBy", columnList = "cid,createdBy")
        }
)
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pid;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String content;

    private String mediaUrl;

    private UUID cid;

    private long likesCount;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> likes;

    @Version
    private long version;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

}
