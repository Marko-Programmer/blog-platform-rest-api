package com.marko.BlogPlatform.service.user;

import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;

public interface UserService {

    public UserResponseDTO register(UserCreateDTO userCreateDTO);

    String verify(LoginRequestDTO loginRequestDTO);

    UserResponseDTO getUserByIdAsDTO(Long userId);
}
