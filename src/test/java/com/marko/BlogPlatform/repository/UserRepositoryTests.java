package com.marko.BlogPlatform.repository;

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
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;



    @Test
    public void findByUsername_ReturnsUser() {
        User user = new User("username", "password","user@post.com", Role.valueOf("ROLE_USER"));


        userRepository.save(user);
        User foundUser = userRepository.findByUsername(user.getUsername());


        Assertions.assertNotNull(foundUser);
        Assertions.assertEquals("username", foundUser.getUsername());
        Assertions.assertEquals("user@post.com", foundUser.getEmail());
        Assertions.assertEquals(Role.valueOf("ROLE_USER"), foundUser.getRole());

    }



    @Test
    public void whenUserDoesNotExist_thenFindByUsernameReturnsNull() {
        User foundUser = userRepository.findByUsername("unknown");
        Assertions.assertNull(foundUser);
    }


    // JpaRepository methods


    @Test
    public void findAll_ReturnsAllUsers() {
        User user1 = new User("username", "password","user@post.com", Role.valueOf("ROLE_USER"));
        User user2 = new User("admin", "password","admin@post.com", Role.valueOf("ROLE_ADMIN"));

        userRepository.save(user1);
        userRepository.save(user2);


        List<User> users = userRepository.findAll();


        Assertions.assertNotNull(users);
        Assertions.assertEquals(2, users.size());
    }



    @Test
    public void findById_ReturnsUser() {
        User user = new User("username", "password","user@post.com", Role.valueOf("ROLE_USER"));

        userRepository.save(user);
        User foundUser = userRepository.findById(user.getId()).orElse(null);

        Assertions.assertNotNull(foundUser);
    }



    @Test
    public void updateUser_ReturnsUpdatedUser() {
        User user = new User("username", "password","user@post.com", Role.valueOf("ROLE_USER"));

        userRepository.save(user);

        User foundUser = userRepository.findById(user.getId()).orElse(null);
        foundUser.setUsername("updatedUsername");
        foundUser.setPassword("updatedPassword");

        User updatedUser = userRepository.save(foundUser);


        Assertions.assertEquals("updatedUsername", updatedUser.getUsername());
        Assertions.assertEquals("updatedPassword", updatedUser.getPassword());
    }



    @Test
    public void deleteUser_ReturnsNull() {
        User user = new User("username", "password","user@post.com", Role.valueOf("ROLE_USER"));

        userRepository.save(user);
        userRepository.delete(user);

        User deletedUser = userRepository.findById(user.getId()).orElse(null);


        Assertions.assertNull(deletedUser);
    }

}