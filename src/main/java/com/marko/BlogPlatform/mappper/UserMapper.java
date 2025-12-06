package com.marko.BlogPlatform.mappper;

import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.model.User;

public class UserMapper {

    public static UserResponseDTO toDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getUsername(), user.getEmail(), user.getRole());
    }

    public static User toEntity(UserCreateDTO dto) {
        return new User(dto.getUsername(), dto.getPassword(), dto.getEmail(), dto.getRole());
    }


}
