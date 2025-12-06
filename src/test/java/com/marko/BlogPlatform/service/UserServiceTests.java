package com.marko.BlogPlatform.service;


import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import com.marko.BlogPlatform.security.JWTService;
import com.marko.BlogPlatform.service.User.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JWTService jwtService;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @InjectMocks
    private UserServiceImpl userService;



    @Test
    public void register_returnUserResponseDTO(){

        UserCreateDTO userCreateDTO = new UserCreateDTO("user","password","user@post.com", Role.ROLE_USER);

        User savedUser = new User("user", encoder.encode("password"), "user@post.com", Role.ROLE_USER);
        savedUser.setId(1L);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);


        UserResponseDTO response = userService.register(userCreateDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("user", response.getUsername());
        Assertions.assertEquals("user@post.com", response.getEmail());
        Assertions.assertEquals(Role.ROLE_USER, response.getRole());
        Assertions.assertEquals(1L, response.getId());
    }



    @Test
    public void login_returnToken(){

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user","password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(jwtService.generateToken(any())).thenReturn("token");


        String token = userService.verify(loginRequestDTO);


        Assertions.assertNotNull(token);
        Assertions.assertEquals("token", token);
    }



    @Test
    public void login_wrongCredentials(){

        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("user","password");

        Authentication authentication = mock(Authentication.class);

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(false);


        String result = userService.verify(loginRequestDTO);


        Assertions.assertEquals("Wrong Credentials", result);
    }

}