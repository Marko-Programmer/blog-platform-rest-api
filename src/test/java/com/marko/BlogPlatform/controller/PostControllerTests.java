package com.marko.BlogPlatform.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.BlogPlatform.dto.PostCreateDTO;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.Arrays;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


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
    private Post post;
    private PostCreateDTO postCreateDTO;

    @BeforeEach
    public void setUp() {
        author = new User("author", "1234", "author@post.com", Role.ROLE_USER);

        post = new Post("title", "content with post", author);
        post.setId(1L);

        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("title dto");
        postCreateDTO.setContent("content with post dto");
    }




    @Test
    public void getPostById_returnsPost() throws Exception {
        post.setId(1L);
        when(postService.getPostById(1L)).thenReturn(post);

        mockMvc.perform(get("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("title"))
                .andExpect(jsonPath("$.content").value("content with post"));
    }

    @Test
    public void getPosts_returnsPosts() throws Exception {

        when(postService.getPosts()).thenReturn(Arrays.asList(post));

        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("title"));
    }




    @Test
    public void addPost_returnsPost() throws Exception {
        when(postService.addPost(postCreateDTO)).thenReturn(post);

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addPost_invalidInput_returnsBadRequest() throws Exception {
        PostCreateDTO invalidDTO = new PostCreateDTO();
        postCreateDTO.setTitle("");
        postCreateDTO.setContent("");

        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.message").exists());
    }




    @Test
    public void updatePost_returnsPost() throws Exception {
        when(postService.updatePost(1L, postCreateDTO)).thenReturn(post);

        mockMvc.perform(put("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postCreateDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePost_invalidInput_returnsBadRequest() throws Exception {
        PostCreateDTO invalidDTO = new PostCreateDTO();
        postCreateDTO.setTitle("");
        postCreateDTO.setContent("");

        mockMvc.perform(put("/posts/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDTO)))
                .andExpect(status().isBadRequest());
    }





    @Test public void deletePost_returnsPost() throws Exception {
        when(postService.deletePost(1L)).thenReturn(null);

        mockMvc.perform(delete("/posts/{id}", 1L))
                .andExpect(status().isNoContent());
    }

}