package com.marko.BlogPlatform.dto.post;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "DTO for returning post information. Contains id, title, content, author id, and timestamps.")
public class PostResponseDTO {

    @Schema(description = "Unique identifier of the post", example = "1")
    private Long id;

    @Schema(description = "Title of the post", example = "My First Blog Post")
    private String title;

    @Schema(description = "Content of the post", example = "This is the content of my first blog post.")
    private String content;

    @Schema(description = "ID of the author of the post", example = "2")
    private Long authorId;

    @Schema(description = "Timestamp when the post was created", example = "2025-10-07T18:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the post was last updated", example = "2025-10-07T19:00:00")
    private LocalDateTime updatedAt;

    public PostResponseDTO() {
    }

    public PostResponseDTO(Long id, String title, String content, Long authorId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.authorId = authorId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public Long getAuthorId() { return authorId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setContent(String content) { this.content = content; }
    public void setAuthorId(Long authorId) { this.authorId = authorId; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}