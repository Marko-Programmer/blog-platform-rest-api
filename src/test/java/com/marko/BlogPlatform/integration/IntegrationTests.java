package com.marko.BlogPlatform.integration;

import com.marko.BlogPlatform.dto.post.PostCreateDTO;
import com.marko.BlogPlatform.dto.post.PostResponseDTO;
import com.marko.BlogPlatform.dto.user.LoginRequestDTO;
import com.marko.BlogPlatform.dto.user.UserCreateDTO;
import com.marko.BlogPlatform.dto.user.UserResponseDTO;
import com.marko.BlogPlatform.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClient;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static com.marko.BlogPlatform.model.Role.ROLE_ADMIN;
import static com.marko.BlogPlatform.model.Role.ROLE_USER;
import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class IntegrationTests {

    @Autowired
    private UserRepository userRepository;

    @LocalServerPort
    private int port;


    private RestClient client;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        client = RestClient.builder()
                .baseUrl("http://localhost:" + port)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }


    @Test
    void registerLoginAndGetMe() {
        // 1 register

        UserCreateDTO user = new UserCreateDTO("Max", "max@post.com", "123", ROLE_USER);
        UserResponseDTO registered = client.post()
                .uri("/auth/register")
                .body(user)
                .retrieve()
                .toEntity(UserResponseDTO.class)
                .getBody();


        assertThat(registered).isNotNull();
        assertThat(registered.getEmail()).isEqualTo("max@post.com");
        assertThat(registered.getId()).isNotNull();

        // 2 login

        LoginRequestDTO loginDTO = new LoginRequestDTO("Max", "123");
        String token = client.post()
                .uri("/auth/login")
                .body(loginDTO)
                .retrieve()
                .toEntity(String.class)
                .getBody();


        assertThat(token).isNotEmpty();

        // 3 get me

        UserResponseDTO me = client.get()
                .uri("/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toEntity(UserResponseDTO.class)
                .getBody();


        assertThat(me).isNotNull();
        assertThat(me.getEmail()).isEqualTo("max@post.com");
        assertThat(me.getId()).isEqualTo(registered.getId());
    }


    @Test
    void postTests() {
        UserCreateDTO userDTO = new UserCreateDTO("user", "user@post.com", "123", ROLE_USER);
        UserResponseDTO registered = client.post()
                .uri("/auth/register")
                .body(userDTO)
                .retrieve()
                .toEntity(UserResponseDTO.class)
                .getBody();

        LoginRequestDTO loginDTO = new LoginRequestDTO("user", "123");
        String token = client.post()
                .uri("/auth/login")
                .body(loginDTO)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        PostCreateDTO postDTO = new PostCreateDTO("Title", "Description");
        PostResponseDTO created = client.post()
                .uri("/posts")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .body(postDTO)
                .retrieve()
                .toEntity(PostResponseDTO.class)
                .getBody();

        assertThat(created).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Title");
        assertThat(created.getContent()).isEqualTo("Description");
    }


    @Test
    public void adminTest() {
        UserCreateDTO userDTO = new UserCreateDTO("admin", "admin@post.com", "123", ROLE_ADMIN);
        UserResponseDTO registered = client.post()
                .uri("/auth/register")
                .body(userDTO)
                .retrieve()
                .toEntity(UserResponseDTO.class)
                .getBody();

        LoginRequestDTO loginDTO = new LoginRequestDTO("admin", "123");
        String token = client.post()
                .uri("/auth/login")
                .body(loginDTO)
                .retrieve()
                .toEntity(String.class)
                .getBody();

        UserResponseDTO[] usersArray = client.get()
                .uri("/admin/users")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .toEntity(UserResponseDTO[].class)
                .getBody();


        List<UserResponseDTO> users = Arrays.asList(usersArray);

        assertThat(users).isNotEmpty();
        assertThat(users.get(0)).isNotNull();
        assertThat(users.get(0).getEmail()).isEqualTo("admin@post.com");
    }



}