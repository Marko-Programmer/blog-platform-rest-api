package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTests {


    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    @Test
    public void loadUserByUsername_returnsUser() {
        String username = "test";
        User user = new User("test", encoder.encode("password"), "user@post.com", Role.ROLE_USER);

        when(userRepository.findByUsername(username)).thenReturn(user);


        UserDetails foundUser = myUserDetailsService.loadUserByUsername(username);

        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals(user.getUsername(), foundUser.getUsername());
    }

}