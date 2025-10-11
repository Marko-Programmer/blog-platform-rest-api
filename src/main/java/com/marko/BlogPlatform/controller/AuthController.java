package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.LoginRequestDTO;
import com.marko.BlogPlatform.dto.UserCreateDTO;
import com.marko.BlogPlatform.dto.UserResponseDTO;
import com.marko.BlogPlatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    private UserService userService;

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns basic user info.",
            requestBody = @RequestBody(
                    required = true,
                    description = "User registration data",
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully registered",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserCreateDTO userCreateDTO) {
        UserResponseDTO userResponseDTO = userService.register(userCreateDTO);
        return ResponseEntity.ok(userResponseDTO);
    }



    @Operation(
            summary = "Login to the platform",
            description = "Authenticates the user and returns a JWT token if credentials are valid.",
            requestBody = @RequestBody(
                    required = true,
                    description = "User login credentials",
                    content = @Content(schema = @Schema(implementation = LoginRequestDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully authenticated. Returns JWT token as string."
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Invalid username or password",
                            content = @Content
                    )
            }
    )
    @PostMapping("/login")
    public String verify(@RequestBody LoginRequestDTO loginRequestDTO) {
        return userService.verify(loginRequestDTO);
    }
}
