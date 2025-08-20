package com.blog.like;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Service
@RequiredArgsConstructor
public class LikeController {
    private final LikeService service;

    @PostMapping("/{postId}")
    public long like(@PathVariable long postId, Authentication auth) {
        return service.like(postId, auth);
    }

    @DeleteMapping("/{postId}")
    public long unlike(@PathVariable long postId, Authentication auth) {
        return service.unlike(postId, auth);
    }
}
