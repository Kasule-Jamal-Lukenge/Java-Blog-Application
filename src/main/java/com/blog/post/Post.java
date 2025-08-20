package com.blog.post;

import com.blog.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length=180)
    private String title;

    @Column(nullable = false, columnDefinition="TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="author_id", nullable=false)
    private User author;

    private Instant createdAt;
    private Instant updatedAt;
}
