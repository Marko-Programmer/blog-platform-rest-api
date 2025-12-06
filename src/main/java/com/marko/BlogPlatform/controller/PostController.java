package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.service.Post.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "Posts", description = "Endpoints for managing blog posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Operation(
            summary = "Get all posts",
            description = "Returns a list of all posts available in the system.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of posts",
                            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<PostResponseDTO>> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    @Operation(
            summary = "Get a post by ID",
            description = "Retrieves a single post based on its unique identifier.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post found",
                            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found",
                            content = @Content
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long id) {
        PostResponseDTO post = postService.getPostById(id);
        return ResponseEntity.ok(post);
    }

    @Operation(
            summary = "Create a new post",
            description = "Creates a new post with the given data and assigns it to the current authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Post successfully created",
                            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content
                    )
            }
    )
    @PostMapping
    public ResponseEntity<PostResponseDTO> addPost(@RequestBody @Valid PostCreateDTO postCreateDTO) {
        PostResponseDTO createdPost = postService.addPost(postCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }


    @Operation(
            summary = "Update an existing post",
            description = "Updates the content and title of an existing post if the user is its author.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Post successfully updated",
                            content = @Content(schema = @Schema(implementation = PostResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — user is not the author of this post",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found",
                            content = @Content
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDTO> updatePost(@PathVariable Long id, @Valid @RequestBody PostCreateDTO postCreateDTO) throws AccessDeniedException {
        PostResponseDTO updated = postService.updatePost(id, postCreateDTO);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a post",
            description = "Deletes a post by its ID if the current user is the author.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "Post successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — user is not the author of this post",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Post not found",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }
}
