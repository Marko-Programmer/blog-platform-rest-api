package com.marko.BlogPlatform.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.service.user.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void register_returnsOk() throws Exception {
        UserCreateDTO userCreateDTO = new UserCreateDTO("John Doe", "john@post.com", "12345678", Role.ROLE_USER);
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "John Doe", "john@post.com", Role.ROLE_USER);

        when(userService.register(any(UserCreateDTO.class))).thenReturn(userResponseDTO);


        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@post.com"));
    }

    @Test
    public void register_withBlankFields_returnsBadRequest() throws Exception {
        UserCreateDTO invalidUser = new UserCreateDTO("", "john@post.com", "12345678", Role.ROLE_USER);

        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(org.hamcrest.Matchers.containsString("Username is required")));
    }



    @Test
    public void verify_returnsOk() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("username", "password");
        String token = "JWT token";

        when(userService.verify(any(LoginRequestDTO.class))).thenReturn(token);


        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    public void verify_withEmptyFields_returnsBadRequest() throws Exception {
        LoginRequestDTO invalidLogin = new LoginRequestDTO("", "12345678");

        mockMvc.perform(post("/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(invalidLogin)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(org.hamcrest.Matchers.containsString("Username is required")));
    }

}
