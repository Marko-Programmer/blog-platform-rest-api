package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.dto.PostCreateDTO;
import com.marko.BlogPlatform.exception.CustomAccessDeniedException;
import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.model.UserPrincipals;
import com.marko.BlogPlatform.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private User author;
    private Post post;
    private PostCreateDTO postCreateDTO;

    @BeforeEach
    public void setUp() {
        author = new User("author", "1234", "author@post.com", Role.ROLE_USER);

        post = new Post("title", "content with post", author);

        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("title dto");
        postCreateDTO.setContent("content with post dto");
    }



    private void mockSecurityContext(User user) {
        Authentication auth = Mockito.mock(Authentication.class);
        UserPrincipals userPrincipals = new UserPrincipals(user);
        when(auth.getPrincipal()).thenReturn(userPrincipals);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(securityContext);
    }



    @Test
    public void getPostById_returnsPost() {
        when(postRepository.findById(any())).thenReturn(Optional.of(post));


        Post foundPost = postService.getPostById(post.getId());

        assertNotNull(foundPost);
        assertEquals(post.getTitle(), foundPost.getTitle());
    }



    @Test
    public void getPostById_throwsResourceNotFoundException_whenPostNotFound() {
        Long postId = 1L;

        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            postService.getPostById(postId);
        });
    }



    @Test
    void addPost_returnsPost() {
        mockSecurityContext(author);
        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        Post savedPost = postService.addPost(postCreateDTO);

        assertNotNull(savedPost);
        assertEquals(author, savedPost.getAuthor());
        verify(postRepository).save(any(Post.class));
    }


    @Test
    public void updatePost_returnsPost() throws Exception {
        Long postId = 1L;
        author.setId(1L);
        mockSecurityContext(author);

        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));


        Post updatedPost = postService.updatePost(postId, postCreateDTO);


        assertNotNull(updatedPost);
        assertEquals("title dto", updatedPost.getTitle());
        assertEquals("content with post dto", updatedPost.getContent());
        assertEquals(author, updatedPost.getAuthor());

        verify(postRepository).findById(postId);
        verify(postRepository).save(any(Post.class));
    }



    @Test
    void updatePost_whenUserNotAuthor_throwsAccessDenied() {
            Long postId = 1L;
            author.setId(1L);

            User anotherUser = new User("hacker", "1234", "hacker@mail.com", Role.ROLE_USER);
            anotherUser.setId(2L);

            mockSecurityContext(anotherUser);

            when(postRepository.findById(postId)).thenReturn(Optional.of(post));


            Assertions.assertThrows(CustomAccessDeniedException.class, () -> {
                postService.updatePost(postId, postCreateDTO);
            });
    }



    @Test
    void deletePost_returnsMessage() {
        Long postId = 1L;
        author.setId(1L);
        mockSecurityContext(author);
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(postId);


        ResponseEntity<String> response = postService.deletePost(postId);


        verify(postRepository, times(1)).deleteById(postId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Post deleted successfully", response.getBody());
    }


    @Test
    void deletePost_whenUserNotAuthor_throwsAccessDenied() {
        Long postId = 1L;
        author.setId(1L);
        post.setId(postId);

        User anotherUser = new User("hacker", "1234", "hacker@mail.com", Role.ROLE_USER);
        anotherUser.setId(2L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        mockSecurityContext(anotherUser);


        Assertions.assertThrows(CustomAccessDeniedException.class, () -> {
            postService.deletePost(postId);
        });

        verify(postRepository, Mockito.never()).deleteById(postId);
    }


    @Test
    void deletePost_whenUserIsAdmin_returnsMessage() {
        Long postId = 1L;
        author.setId(1L);
        post.setId(postId);

        User anotherUser = new User("admin", "1234", "hacker@mail.com", Role.ROLE_ADMIN);
        anotherUser.setId(2L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));

        mockSecurityContext(anotherUser);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(postId);


        ResponseEntity<String> response = postService.deletePost(postId);


        verify(postRepository, times(1)).deleteById(postId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("Post deleted successfully", response.getBody());
    }


}
