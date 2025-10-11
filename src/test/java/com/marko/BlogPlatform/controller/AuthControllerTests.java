package com.marko.BlogPlatform.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.marko.BlogPlatform.dto.LoginRequestDTO;
import com.marko.BlogPlatform.dto.UserCreateDTO;
import com.marko.BlogPlatform.dto.UserResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.service.UserService;
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
        UserCreateDTO userCreateDTO = new UserCreateDTO("username", "email", "password", Role.ROLE_USER);
        UserResponseDTO userResponseDTO = new UserResponseDTO(1L, "username", "email", Role.ROLE_USER);

        when(userService.register(any(UserCreateDTO.class))).thenReturn(userResponseDTO);


        mockMvc.perform(post("/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(userCreateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.email").value("email"));
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




}
