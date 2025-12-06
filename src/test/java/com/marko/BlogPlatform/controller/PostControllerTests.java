package com.marko.BlogPlatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.service.Post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class PostControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;


    private User author;
    private PostResponseDTO postResponseDTO;
    private PostCreateDTO postCreateDTO;

    @BeforeEach
    public void setUp() {
        // Автор DTO
        author = new User("author", "1234", "author@post.com", Role.ROLE_USER);
        author.setId(1L);

        postResponseDTO = new PostResponseDTO(
                1L,
                "title",
                "content with post",
                author.getId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("title dto");
        postCreateDTO.setContent("content with post dto");
    }

    @Test
    public void getPostById_returnsPost() throws Exception {
        when(postService.getPostById(1L)).thenReturn(postResponseDTO);

        mockMvc.perform(get("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(postResponseDTO.getId()))
                .andExpect(jsonPath("$.title").value(postResponseDTO.getTitle()))
                .andExpect(jsonPath("$.content").value(postResponseDTO.getContent()))
                .andExpect(jsonPath("$.authorId").value(postResponseDTO.getAuthorId()));
    }

    @Test
    public void getPosts_returnsPosts() throws Exception {
        when(postService.getPosts()).thenReturn(Arrays.asList(postResponseDTO));

        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value(postResponseDTO.getTitle()))
                .andExpect(jsonPath("$[0].authorId").value(postResponseDTO.getAuthorId()));
    }

    @Test
    public void addPost_returnsPost() throws Exception {
        PostResponseDTO createdDTO = new PostResponseDTO(
                2L,
                postCreateDTO.getTitle(),
                postCreateDTO.getContent(),
                author.getId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postService.addPost(postCreateDTO)).thenReturn(createdDTO);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isCreated());
    }


    @Test
    public void addPost_invalidInput_returnsBadRequest() throws Exception {
        PostCreateDTO invalidDTO = new PostCreateDTO();
        invalidDTO.setTitle("");
        invalidDTO.setContent("");

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    public void updatePost_returnsPost() throws Exception {
        PostResponseDTO updatedDTO = new PostResponseDTO(
                postResponseDTO.getId(),
                postCreateDTO.getTitle(),
                postCreateDTO.getContent(),
                author.getId(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(postService.updatePost(1L, postCreateDTO)).thenReturn(updatedDTO);

        mockMvc.perform(put("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void deletePost_returnsNoContent() throws Exception {
        doNothing().when(postService).deletePost(1L);

        mockMvc.perform(delete("/posts/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
