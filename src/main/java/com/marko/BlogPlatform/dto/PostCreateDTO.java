package com.marko.BlogPlatform.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO for creating a new post. Contains the title and content of the post")
public class PostCreateDTO {

    @Schema(description = "Post Title", example = "My First Post")
    @NotBlank(message = "Title is required")
    private String title;

    @Schema(description = "Post content (at least 10 characters)", example = "This is my first blog post.")
    @NotBlank(message = "Content is required")
    @Size(min = 10, message = "Content must be at least 10 characters")
    private String content;

    public PostCreateDTO() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "PostCreateDTO{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}
