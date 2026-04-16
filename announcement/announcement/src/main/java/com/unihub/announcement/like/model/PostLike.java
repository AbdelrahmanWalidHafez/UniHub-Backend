package com.unihub.announcement.like.model;

import com.unihub.announcement.common.model.BaseEntity;
import com.unihub.announcement.post.model.Post;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
        @Index(name = "idx_post_like_post_createdBy", columnList = "post_id, createdBy")
})
public class PostLike extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID lid;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

}
