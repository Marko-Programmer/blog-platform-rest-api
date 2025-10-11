package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.model.UserPrincipals;
import com.marko.BlogPlatform.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public void  getAllUsers_asAdmin_returnsUsers() throws Exception {
        User admin = new User("admin", "1234", "admin@post.com", Role.ROLE_ADMIN);
        User user1 = new User("user1", "1234", "user1@post.com", Role.ROLE_USER);

        when(adminService.getAllUsers()).thenReturn(Arrays.asList(admin, user1));

        UserPrincipals adminPrincipals = new UserPrincipals(admin);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(adminPrincipals, null, adminPrincipals.getAuthorities())
        );

        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("user1"));
    }

    @Test
    @WithMockUser(roles = "USER")
    public void  getAllUsers_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(get("/admin/users"))
                .andExpect(status().isForbidden());
    }



    @Test
    @WithMockUser(roles = "ADMIN")
    public void deleteUser_asAdmin_returnsNoContent() throws Exception {
        doNothing().when(adminService).deleteUser(1L);

        mockMvc.perform(delete("/admin/users/{id}", 1L))
                .andExpect(status().isNoContent());
    }


    @Test
    @WithMockUser(roles = "USER")
    public void  deleteUser_asUser_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/admin/users/{id}", 1L))
                .andExpect(status().isForbidden());
    }






}
