package com.marko.BlogPlatform.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import com.marko.BlogPlatform.model.Role;

@Schema(description = "DTO for creating a new User. Contains username, email, password, and role.")
public class UserCreateDTO {


    @Schema(description = "User username", example = "John Doe")
    @NotBlank(message = "Username is required")
    @JsonProperty("username")
    private String username;


    @Schema(description = "User email", example = "john@post.com")
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    @JsonProperty("email")
    private String email;

    @Schema(description = "User password", example = "12345678")
    @NotBlank(message = "Password is required")
    @JsonProperty("password")
    private String password;

    @Schema(description = "Role of the user", example = "USER")
    @JsonProperty("role")
    private Role role;

    public UserCreateDTO() { }

    public UserCreateDTO(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }


    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public Role getRole() { return role; }

    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(Role role) { this.role = role; }
}
