package com.blog.comment;

import com.blog.comment.dto.CommentDtos;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping("post/{postId}")
    public List<CommentDtos.CommentResponse> list(@PathVariable Long postId){
        return commentService.list(postId);
    }

    @PostMapping("/post/{postId}")
    public ResponseEntity<?> add(@PathVariable Long postId, @Valid @RequestBody CommentDtos.CommentCreateRequest req, Authentication auth){
        return ResponseEntity.ok(commentService.add(postId, req, auth));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody CommentDtos.CommentCreateRequest req, Authentication auth){
        commentService.update(id, req, auth);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        commentService.delete(id, auth);
        return ResponseEntity.noContent().build();
    }
}
