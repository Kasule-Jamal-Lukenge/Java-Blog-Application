package com.blog.comment.dto;

import jakarta.validation.constraints.NotBlank;

public class CommentDtos {
    public record CommentCreateRequest(@NotBlank String text){}
    public record CommentResponse(Long id, String text, String author, String createdAt, String updatedAt){}
}
