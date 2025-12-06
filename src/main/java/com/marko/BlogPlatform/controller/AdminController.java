package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@Tag(name = "Admin", description = "Endpoints for administrative user management actions")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(
            summary = "Get list of all users",
            description = "Retrieves a list of all registered users. Available only for users with ADMIN role.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved list of users",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — only admins can access this resource",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }



    @Operation(
            summary = "Get user by id",
            description = "Retrieves a registered user by id. Available only for users with ADMIN role.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user by id",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — only admins can access this resource",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(adminService.getUserById(userId));
    }





    @Operation(
            summary = "Delete user by ID",
            description = "Deletes a user by their ID. Available only for users with ADMIN role.",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "User successfully deleted"
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Access denied — only admins can delete users",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
                            content = @Content
                    )
            }
    )
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
