package com.marko.BlogPlatform.service;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.exception.CustomAccessDeniedException;
import com.marko.BlogPlatform.exception.ResourceNotFoundException;
import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import com.marko.BlogPlatform.repository.PostRepository;
import com.marko.BlogPlatform.security.PostPermissionValidator;
import com.marko.BlogPlatform.security.SecurityUtil;
import com.marko.BlogPlatform.service.Post.PostServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PostServiceTests {

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostPermissionValidator permissionValidator;

    @InjectMocks
    private PostServiceImpl postService;

    private User author;
    private Post post;
    private PostCreateDTO postCreateDTO;

    @BeforeEach
    public void setUp() {
        author = new User("author", "1234", "author@post.com", Role.ROLE_USER);
        author.setId(1L);

        post = new Post("title", "content with post", author);
        post.setId(1L);

        postCreateDTO = new PostCreateDTO();
        postCreateDTO.setTitle("title dto");
        postCreateDTO.setContent("content with post dto");
    }

    private void mockSecurityUtil(User user, Runnable testLogic) {
        try (MockedStatic<SecurityUtil> mocked = Mockito.mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::getCurrentUser).thenReturn(user);
            testLogic.run();
        }
    }

    @Test
    void getPostById_returnsPostDTO() {
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        PostResponseDTO result = postService.getPostById(post.getId());

        assertNotNull(result);
        assertEquals(post.getTitle(), result.getTitle());
        assertEquals(post.getContent(), result.getContent());
        assertEquals(author.getId(), result.getAuthorId());
    }

    @Test
    void getPostById_throwsResourceNotFoundException() {
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> postService.getPostById(1L));
    }

    @Test
    void addPost_returnsPostDTO() {
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        mockSecurityUtil(author, () -> {
            PostResponseDTO result = postService.addPost(postCreateDTO);

            assertNotNull(result);
            assertEquals(author.getId(), result.getAuthorId());
            assertEquals(postCreateDTO.getTitle(), result.getTitle());
            assertEquals(postCreateDTO.getContent(), result.getContent());
            verify(postRepository).save(any(Post.class));
        });
    }

    @Test
    void updatePost_returnsPostDTO() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));
        doNothing().when(permissionValidator).validateAuthor(any(User.class), any(Post.class));

        mockSecurityUtil(author, () -> {
            PostResponseDTO result = null;
            try {
                result = postService.updatePost(post.getId(), postCreateDTO);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            assertNotNull(result);
            assertEquals(postCreateDTO.getTitle(), result.getTitle());
            assertEquals(postCreateDTO.getContent(), result.getContent());
            assertEquals(author.getId(), result.getAuthorId());

            verify(postRepository).findById(post.getId());
            verify(postRepository).save(any(Post.class));
            verify(permissionValidator).validateAuthor(any(User.class), any(Post.class));
        });
    }

    @Test
    void updatePost_whenUserNotAuthor_throwsAccessDenied() {
        User hacker = new User("hacker", "1234", "hacker@mail.com", Role.ROLE_USER);
        hacker.setId(2L);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        doThrow(new CustomAccessDeniedException("Not allowed"))
                .when(permissionValidator).validateAuthor(any(User.class), any(Post.class));

        mockSecurityUtil(hacker, () -> {
            assertThrows(CustomAccessDeniedException.class,
                    () -> postService.updatePost(post.getId(), postCreateDTO));
            verify(postRepository).findById(post.getId());
            verify(permissionValidator).validateAuthor(any(User.class), any(Post.class));
            verify(postRepository, never()).save(any(Post.class));
        });
    }

    @Test
    void deletePost_deletesPost_whenUserIsAuthor() {
        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(post.getId());
        doNothing().when(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));

        mockSecurityUtil(author, () -> {
            postService.deletePost(post.getId());
            verify(postRepository).deleteById(post.getId());
            verify(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));
        });
    }

    @Test
    void deletePost_throwsAccessDenied_whenUserNotAuthorOrAdmin() {
        User hacker = new User("hacker", "1234", "hacker@mail.com", Role.ROLE_USER);
        hacker.setId(2L);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        doThrow(new CustomAccessDeniedException("Not allowed"))
                .when(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));

        mockSecurityUtil(hacker, () -> {
            assertThrows(CustomAccessDeniedException.class,
                    () -> postService.deletePost(post.getId()));
            verify(postRepository, never()).deleteById(post.getId());
            verify(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));
        });
    }

    @Test
    void deletePost_deletesPost_whenUserIsAdmin() {
        User admin = new User("admin", "1234", "admin@mail.com", Role.ROLE_ADMIN);
        admin.setId(2L);

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        doNothing().when(postRepository).deleteById(post.getId());
        doNothing().when(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));

        mockSecurityUtil(admin, () -> {
            postService.deletePost(post.getId());
            verify(postRepository).deleteById(post.getId());
            verify(permissionValidator).validateDeletePermission(any(User.class), any(Post.class));
        });
    }
}
