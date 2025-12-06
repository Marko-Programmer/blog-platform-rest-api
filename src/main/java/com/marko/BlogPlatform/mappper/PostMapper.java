package com.marko.BlogPlatform.mappper;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {

    public static PostResponseDTO toDTO(Post post) {
        return new PostResponseDTO(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getAuthor() != null ? post.getAuthor().getId() : null,
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    public static List<PostResponseDTO> toDTO(List<Post> posts) {
        return posts.stream()
                .map(PostMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static Post toEntity(PostCreateDTO dto) {
        return new Post(dto.getTitle(), dto.getContent(), null);
    }

}
