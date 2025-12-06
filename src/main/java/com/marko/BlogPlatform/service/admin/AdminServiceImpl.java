package com.marko.BlogPlatform.service.admin;

import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.mappper.UserMapper;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(AdminServiceImpl.class);

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(UserMapper::toDTO)
                .toList();
    }

    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return UserMapper.toDTO(user);
    }

    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));

        userRepository.delete(userToDelete);
        logger.info("Deleting user: {}", userToDelete);
    }


}