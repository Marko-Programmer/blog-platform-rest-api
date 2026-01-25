package com.marko.BlogPlatform.dto.user;

import com.marko.BlogPlatform.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO for returning user information. Contains id, username, email, and role.")
public class UserResponseDTO {

    @Schema(description = "Unique identifier of the user", example = "1")
    private Long id;

    @Schema(description = "Username of the user", example = "John Doe")
    private String username;

    @Schema(description = "Email of the user", example = "john@post.com")
    private String email;

    @Schema(description = "Role of the user", example = "USER")
    private Role role;


    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String username, String email, Role role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }

    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(Role role) { this.role = role; }
}
