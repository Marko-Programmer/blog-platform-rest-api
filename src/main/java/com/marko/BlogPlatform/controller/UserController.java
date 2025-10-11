package com.marko.BlogPlatform.controller;

import com.marko.BlogPlatform.dto.UserResponseDTO;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.model.UserPrincipals;
import com.marko.BlogPlatform.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {


    @Operation(
            summary = "Get current logged-in user",
            description = "Returns information about the currently authenticated user.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successfully retrieved user info",
                            content = @Content(schema = @Schema(implementation = UserResponseDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized — user is not authenticated",
                            content = @Content
                    )
            }
    )
    @GetMapping("/me")
    public UserResponseDTO getCurrentUser(@AuthenticationPrincipal UserPrincipals userPrincipals) {
        User user = userPrincipals.getUser();
        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }




}