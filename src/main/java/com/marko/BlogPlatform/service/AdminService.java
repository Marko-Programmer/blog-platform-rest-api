package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    private Logger logger = LoggerFactory.getLogger(AdminService.class);


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
    }



    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(()-> {
                    return new ResourceNotFoundException("User " + id + " not found");
                });
        logger.info("Deleting user: {}", userToDelete);
        userRepository.delete(userToDelete);
    }



}