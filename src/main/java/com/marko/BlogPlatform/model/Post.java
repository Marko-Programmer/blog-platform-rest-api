package com.marko.BlogPlatform.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
@Schema(description = "Entity representing a blog post. Contains title, content, author, and timestamps.")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    @Schema(description = "Unique identifier of the post", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Title of the post", example = "My First Blog Post")
    private String title;

    @Column(nullable = false)
    @Size(min = 10, message = "Content must be at least 10 characters")
    @Schema(description = "Content of the post (minimum 10 characters)", example = "This is the content of my first blog post.")
    private String content;

    @ManyToOne(optional = false)
    @JoinColumn(name = "author_id")
    @JsonBackReference
    @Schema(description = "Author of the post")
    private User author;

    @Column(nullable = false)
    @Schema(description = "Timestamp when the post was created", example = "2025-10-07T18:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when the post was last updated", example = "2025-10-07T19:00:00")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Post() {
    }

    public Post(String title, String content, User author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", author=" + author +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

}