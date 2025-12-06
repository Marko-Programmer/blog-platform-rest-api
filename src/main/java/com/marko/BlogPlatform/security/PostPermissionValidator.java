package com.marko.BlogPlatform.security;

import com.marko.BlogPlatform.exception.CustomAccessDeniedException;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import org.springframework.stereotype.Component;

@Component
public class PostPermissionValidator {

    public void validateAuthor(User currentUser, Post post) {
        if (!post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You are not the author of this post");
        }
    }

    public void validateDeletePermission(User currentUser, Post post) {
        if (currentUser.getRole() != Role.ROLE_ADMIN
                && !post.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You are not allowed to delete this post");
        }
    }
}
