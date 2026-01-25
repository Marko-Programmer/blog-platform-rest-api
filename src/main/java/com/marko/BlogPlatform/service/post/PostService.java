package com.marko.BlogPlatform.service.post;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;

import java.nio.file.AccessDeniedException;
import java.util.List;

public interface PostService {

    List<PostResponseDTO> getPosts();

    PostResponseDTO getPostById(Long id);

    PostResponseDTO addPost(PostCreateDTO postCreateDTO);

    PostResponseDTO updatePost(Long id, PostCreateDTO postCreateDTO) throws AccessDeniedException;

    void deletePost(Long id);

}
