package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.dto.PostCreateDTO;
import com.marko.BlogPlatform.exception.CustomAccessDeniedException;
import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.model.UserPrincipals;
import com.marko.BlogPlatform.repository.PostRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    private Logger logger = LoggerFactory.getLogger(PostService.class);


    public List<Post> getPosts() {
        return postRepository.findAll();
    }


    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));
    }


    public Post addPost(PostCreateDTO postCreateDTO) {
        UserPrincipals userPrincipals = (UserPrincipals) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        User author = userPrincipals.getUser();

        Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getContent(), author);

        logger.info("Adding post: {}", post);

        return postRepository.save(post);
    }



    public Post updatePost(Long id, PostCreateDTO postCreateDTO) throws AccessDeniedException {

        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));

        UserPrincipals userPrincipals = (UserPrincipals) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User currentUser = userPrincipals.getUser();

        if(!oldPost.getAuthor().getId().equals(currentUser.getId())) {
            throw new CustomAccessDeniedException("You are not the author of this post");
        }

        oldPost.setTitle(postCreateDTO.getTitle());
        oldPost.setContent(postCreateDTO.getContent());;

        logger.info("Updated post: {}", oldPost);

        return postRepository.save(oldPost);
    }


    public ResponseEntity<String> deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));

        UserPrincipals userPrincipals = (UserPrincipals) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        User currentUser = userPrincipals.getUser();

        if (currentUser.getRole() != Role.ROLE_ADMIN && !post.getAuthor().getId().equals(currentUser.getId())) {
            logger.warn("You are not the author of this post");
            throw new CustomAccessDeniedException("You are not allowed to delete this post");
        }


        postRepository.deleteById(id);
        logger.info("Deleted post: {}", post);
        return ResponseEntity.ok("Post deleted successfully");
    }



}