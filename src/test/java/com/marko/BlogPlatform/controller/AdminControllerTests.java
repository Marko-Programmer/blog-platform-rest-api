package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.service.admin.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = true)
public class AdminControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getAllUsers_asAdmin_returnsUsers() throws Exception {
        UserResponseDTO admin = new UserResponseDTO(1L, "admin", "admin@post.com", Role.ROLE_ADMIN);
        UserResponseDTO user1 = new UserResponseDTO(2L, "user1", "user1@post.com", Role.ROLE_USER);

        when(adminService.getAllUsers()).thenReturn(Arrays.asList(admin, user1));

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("user1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getAllUsers_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void getUserById_asAdmin_returnsUser() throws Exception {
        UserResponseDTO user = new UserResponseDTO(2L, "user1", "user1@post.com", Role.ROLE_USER);

        when(adminService.getUserById(2L)).thenReturn(user);

        mockMvc.perform(get("/admin/users/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andExpect(jsonPath("$.email").value("user1@post.com"))
                .andExpect(jsonPath("$.role").value("ROLE_USER"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void getUserById_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/users/2"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void deleteUser_asAdmin_returnsNoContent() throws Exception {
        doNothing().when(adminService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    public void deleteUser_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/admin/users/{id}", 1L))
                .andExpect(status().isForbidden());
    }
}
