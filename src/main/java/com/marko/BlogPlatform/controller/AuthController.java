package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.service.User.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Endpoints for user registration and login")
public class AuthController {

    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account and returns basic user info.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "User registration data",
                    content = @Content(schema = @Schema(implementation = UserCreateDTO.class))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "User successfully registered",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request data",
                            content = @Content
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserCreateDTO userCreateDTO) {
        logger.info("Registration attempt: username={}, email={}, role={}",
                userCreateDTO.getUsername(),
                userCreateDTO.getEmail(),
                userCreateDTO.getRole());
        UserResponseDTO userResponseDTO = userService.register(userCreateDTO);
        return ResponseEntity.ok(userResponseDTO);
    }



    @Operation(
            summary = "Login to the platform",
            description = "Authenticates the user and returns a JWT token if credentials are valid.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
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
    public ResponseEntity<String> verify(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
        logger.info("Login attempt by user: {}", loginRequestDTO.getUsername());
        return ResponseEntity.ok(userService.verify(loginRequestDTO));
    }
}
