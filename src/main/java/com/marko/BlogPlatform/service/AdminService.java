package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public void deleteUser(Long id) {
        User userToDelete = userRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("User Not Found"));

        userRepository.delete(userToDelete);
    }


}