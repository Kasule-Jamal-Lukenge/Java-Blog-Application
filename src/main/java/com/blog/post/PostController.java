package com.blog.post;

import com.blog.post.dto.PostDtos;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;


@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class PostController {
    private final PostService postService;

    @GetMapping
    public Page<PostDtos.PostListItem> list(@RequestParam(defaultValue="0")int page, @RequestParam(defaultValue="10") int size){
        return postService.list(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    public PostDtos.PostResponse get(@PathVariable Long id){
        return postService.get(id);
    }

    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody PostDtos.PostCreateRequest req, Authentication auth){
        var id = postService.create(req, auth);
        return ResponseEntity.ok(id);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody PostDtos.PostCreateRequest req, Authentication auth){
        postService.update(id, req, auth);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id, Authentication auth){
        postService.delete(id, auth);
        return ResponseEntity.noContent().build();
//        return ResponseEntity.noContent().build();
    }
}
