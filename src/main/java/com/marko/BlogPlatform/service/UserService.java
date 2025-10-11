package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.dto.LoginRequestDTO;
import com.marko.BlogPlatform.dto.UserCreateDTO;
import com.marko.BlogPlatform.dto.UserResponseDTO;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;


    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);


    public UserResponseDTO register(UserCreateDTO userCreateDTO) {
        userCreateDTO.setPassword(encoder.encode(userCreateDTO.getPassword()));

        Role role = userCreateDTO.getRole() != null ? userCreateDTO.getRole() : Role.ROLE_USER;

        User user = new User(
                userCreateDTO.getUsername(),
                userCreateDTO.getPassword(),
                userCreateDTO.getEmail(),
                role
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }


    public String verify(LoginRequestDTO loginRequestDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        if (auth.isAuthenticated()) {
            return jwtService.generateToken(loginRequestDTO.getUsername());
        }
        return "Wrong Credentials";
    }


}
