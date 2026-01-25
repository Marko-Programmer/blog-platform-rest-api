package com.marko.BlogPlatform.service.User;

import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.mappper.UserMapper;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import com.marko.BlogPlatform.security.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JWTService jwtService;

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public UserResponseDTO register(UserCreateDTO userCreateDTO) {
        userCreateDTO.setPassword(encoder.encode(userCreateDTO.getPassword()));

        if (userCreateDTO.getRole() == null) {
            userCreateDTO.setRole(Role.ROLE_USER);
        }

        User savedUser = userRepository.save(UserMapper.toEntity(userCreateDTO));

        logger.info("New user registered: {}", savedUser);

        return UserMapper.toDTO(savedUser);
    }

    public String verify(LoginRequestDTO loginRequestDTO) {
        Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken
                (loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

        if (auth.isAuthenticated()) {
            logger.info("User {} logged in", loginRequestDTO.getUsername());
            return jwtService.generateToken(loginRequestDTO.getUsername());
        }

        logger.warn("Failed login attempt for user {}", loginRequestDTO.getUsername());
        return "Wrong Credentials";
    }

    @Override
    public UserResponseDTO getUserByIdAsDTO(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return UserMapper.toDTO(user);
    }


}
