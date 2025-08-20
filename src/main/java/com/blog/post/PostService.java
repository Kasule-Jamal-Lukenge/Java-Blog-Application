package com.blog.post;

import com.blog.like.PostLikeRepository;
import com.blog.post.dto.PostDtos;
import com.blog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final PostLikeRepository likeRepo;

    private String excerpt(String content){
        var plain = content.trim();
        return plain.length() <= 160 ? plain : plain.substring(0, 160) + "...";
    }

    public Page<PostDtos.PostListItem> list(Pageable pageable){
        return postRepo.findAllByOrderByCreatedAtDesc(pageable)
                .map(p -> new PostDtos.PostListItem(
                        p.getId(),
                        p.getTitle(),
                        excerpt(p.getContent()),
                        p.getAuthor().getUsername(),
                        p.getCreatedAt().toString(),
                        likeRepo.countByPostId(p.getId())
                ));
    }

    public PostDtos.PostResponse get(Long id){
        var p = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Post Not Found"));
        return new PostDtos.PostResponse(
                p.getId(),
                p.getTitle(),
                excerpt(p.getContent()),
                p.getContent(),
                p.getAuthor().getUsername(),
                p.getCreatedAt().toString(),
                p.getUpdatedAt() == null ? null : p.getUpdatedAt().toString(),
                likeRepo.countByPostId(p.getId())
        );
    }

    public Long create(PostDtos.PostCreateRequest req, Authentication auth){
        var user = userRepo.findByUsername(auth.getName()).orElseThrow();
        var post = Post.builder()
                .title(req.title())
                .content(req.content())
                .author(user)
                .createdAt(Instant.now())
                .build();
        return postRepo.save(post).getId();
    }

    public void update(Long id, PostDtos.PostCreateRequest req, Authentication auth){
        var post = postRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Post Not Found"));
        if(!post.getAuthor().getUsername().equals(auth.getName())) {
            throw new IllegalArgumentException("You do not have permission to update this post");
        }
        post.setTitle(req.title());
        post.setContent(req.content());
        post.setUpdatedAt(Instant.now());
        postRepo.save(post);
    }

    public void delete(Long id, Authentication auth){
        var post = postRepo.findById(id).orElseThrow();
        if(!post.getAuthor().getUsername().equals(auth.getName())) {
            throw new IllegalArgumentException("Only The Author Can Delete This Post");
        }
        postRepo.delete(post);
    }
}
