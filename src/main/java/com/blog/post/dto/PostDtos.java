package com.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class PostDtos {
    public record PostCreateRequest(
            @NotBlank
            @Size(max=180)
            String title,

            @NotBlank
            String content
    ){}
    public record PostResponse(
            Long id,
            String title,
            String excerpt,
            String content,
            String author,
            String createdAt,
            String updatedAt,
            long likeCount
    ){}

    public record PostListItem(
            Long id,
            String title,
            String excerpt,
            String author,
            String createdAt,
            long likeCount
    ){}
}
