package com.marko.BlogPlatform.service.admin;

import com.marko.BlogPlatform.dto.user.UserResponseDTO;

import java.util.List;

public interface AdminService {

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO getUserById(Long userId);

    void deleteUser(Long id);

}
