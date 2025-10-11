package com.marko.BlogPlatform.repository;

import com.marko.BlogPlatform.model.Post;
import com.marko.BlogPlatform.model.Role;
import com.marko.BlogPlatform.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class PostRepositoryTests {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;


    // JpaRepository methods


    @Test
    public void findAll_returnsPosts() {
        User author = new User("author", "1234", "author@post.com", Role.valueOf("ROLE_USER"));
        userRepository.save(author);

        Post post1 = new Post("title1", "content with post1", author);
        Post post2 = new Post("title2", "content with post2", author);

        postRepository.save(post1);
        postRepository.save(post2);


        List<Post> posts = postRepository.findAll();


        Assertions.assertNotNull(posts);
        Assertions.assertEquals(2, posts.size());
    }



    @Test
    public void findById_returnsPost() {
        User author = new User("author", "1234", "author@post.com", Role.valueOf("ROLE_USER"));
        userRepository.save(author);

        Post post = new Post("title", "content with post", author);

        postRepository.save(post);


        Post foundedPost = postRepository.findById(post.getId()).orElse(null);


        Assertions.assertNotNull(foundedPost);
        Assertions.assertEquals("title", foundedPost.getTitle());

    }



    @Test
    public void updatePost_ReturnsUpdatedPost() {
        User author = new User("author", "1234", "author@post.com", Role.valueOf("ROLE_USER"));
        userRepository.save(author);

        Post post = new Post("title", "content with post", author);

        postRepository.save(post);

        Post foundPost = postRepository.findById(post.getId()).orElse(null);
        foundPost.setTitle("updatedTitle");
        foundPost.setContent("updatedContent");

        Post updatedPost = postRepository.save(foundPost);


        Assertions.assertEquals("updatedTitle", updatedPost.getTitle());
        Assertions.assertEquals("updatedContent", updatedPost.getContent());
    }



    @Test
    public void deleteUser_ReturnsNull() {
        User author = new User("author", "1234", "author@post.com", Role.valueOf("ROLE_USER"));
        userRepository.save(author);

        Post post = new Post("title", "content with post", author);

        postRepository.save(post);
        postRepository.delete(post);

        Post deletedPost = postRepository.findById(post.getId()).orElse(null);


        Assertions.assertNull(deletedPost);
    }

}