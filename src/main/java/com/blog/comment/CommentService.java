package com.blog.comment;

import com.blog.comment.dto.CommentDtos;
import com.blog.post.PostRepository;
import com.blog.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final UserRepository userRepo;
    private final PostRepository postRepo;

    public List<CommentDtos.CommentResponse> list(Long postId){
        return commentRepo.findByPostIdOrderByCreatedAtAsc(postId).stream().map(
                c -> new CommentDtos.CommentResponse(
                        c.getId(),
                        c.getText(),
                        c.getAuthor().getUsername(),
                        c.getCreatedAt().toString(),
                        c.getUpdatedAt()==null?null:c.getUpdatedAt().toString()
                )
        ).toList();
    }

    public Long add(Long postId, CommentDtos.CommentCreateRequest req, Authentication auth){
        var user = userRepo.findByUsername(auth.getName()).orElseThrow(() -> new IllegalArgumentException("User Not Found"));
        var post = postRepo.findById(postId).orElseThrow(() -> new IllegalArgumentException("Post Not Found"));
        var comment = Comment.builder()
                .text(req.text())
                .author(user)
                .post(post)
                .createdAt(Instant.now())
                .build();
        return commentRepo.save(comment).getId();
    }

    public void update(Long id, CommentDtos.CommentCreateRequest req, Authentication auth){
        var comment = commentRepo.findById(id).orElseThrow(() -> new IllegalArgumentException("Post Not Found"));
        if(!comment.getAuthor().getUsername().equals(auth.getName())) {
            throw new IllegalArgumentException("Only The Author Can Edit This Comment");
        }
        comment.setText(req.text());
        comment.setUpdatedAt(Instant.now());
        commentRepo.save(comment);
    }

    public void delete(Long id, Authentication auth){
        var comment = commentRepo.findById(id).orElseThrow();
        if(!comment.getAuthor().getUsername().equals(auth.getName())) {
            throw new IllegalArgumentException("Only The Author Can Delete This Comment");
        }
        commentRepo.delete(comment);
    }
}
