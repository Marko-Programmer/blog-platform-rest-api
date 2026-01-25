package com.marko.BlogPlatform.service.post;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.mappper.PostMapper;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.PostRepository;
import com.marko.BlogPlatform.security.PostPermissionValidator;
import com.marko.BlogPlatform.security.SecurityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Service
public class PostServiceImpl  implements  PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostPermissionValidator permissionValidator;

    private Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    public List<PostResponseDTO> getPosts() {
        return PostMapper.toDTO(postRepository.findAll());
    }




    public PostResponseDTO getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));
        return PostMapper.toDTO(post);
    }


    public PostResponseDTO addPost(PostCreateDTO postCreateDTO) {
        User author = SecurityUtil.getCurrentUser();

        Post post = new Post(postCreateDTO.getTitle(), postCreateDTO.getContent(), author);

        logger.info("Adding post: {}", post);

        Post savedPost = postRepository.save(post);
        return PostMapper.toDTO(savedPost);
    }


    public PostResponseDTO updatePost(Long id, PostCreateDTO postCreateDTO) throws AccessDeniedException {

        Post oldPost = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));

        User currentUser = SecurityUtil.getCurrentUser();

        permissionValidator.validateAuthor(currentUser, oldPost);

        oldPost.setTitle(postCreateDTO.getTitle());
        oldPost.setContent(postCreateDTO.getContent());;

        logger.info("Updated post: {}", oldPost);

        Post savedPost = postRepository.save(oldPost);
        return PostMapper.toDTO(savedPost);
    }


    public void deletePost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Post found with id: " + id));

        User currentUser = SecurityUtil.getCurrentUser();

        permissionValidator.validateDeletePermission(currentUser, post);

        postRepository.deleteById(id);
        logger.info("Deleted post: {}", post);
    }

}