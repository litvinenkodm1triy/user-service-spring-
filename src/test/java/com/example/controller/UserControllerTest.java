package com.example.controller;

import com.example.UserServiceApplication;
import com.example.config.TestKafkaConfig;
import com.example.dto.request.UserRequest;
import com.example.entity.User;
import com.example.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = {UserServiceApplication.class, TestKafkaConfig.class})
@AutoConfigureMockMvc
@Testcontainers
class UserControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");

        registry.add("spring.kafka.bootstrap-servers", () -> "localhost:9999");
        registry.add("spring.kafka.producer.properties.allow.auto.create.topics", () -> "false");
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void cleanUp() {
        userRepository.deleteAll();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("Иван Петров");
        request.setEmail("ivan@example.com");
        request.setAge(30);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name").value("Иван Петров"))
                .andExpect(jsonPath("$.email").value("ivan@example.com"))
                .andExpect(jsonPath("$.age").value(30))
                .andExpect(jsonPath("$.createdAt", notNullValue()))
                .andExpect(jsonPath("$.updatedAt", notNullValue()));
    }

    @Test
    void getUser_WhenExists_ShouldReturnUser() throws Exception {
        User user = new User();
        user.setName("Тест");
        user.setEmail("test@mail.com");
        user.setAge(25);
        User saved = userRepository.save(user);

        mockMvc.perform(get("/api/v1/users/{id}", saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.name").value("Тест"));
    }

    @Test
    void getUser_WhenNotFound_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/v1/users/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Не найден пользователь с id: 999"));
    }

    @Test
    void updateUser_ShouldUpdateAndReturnUpdated() throws Exception {
        User user = new User();
        user.setName("Старое имя");
        user.setEmail("old@mail.com");
        user.setAge(20);
        User saved = userRepository.save(user);

        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Новое имя");
        updateRequest.setEmail("new@mail.com");
        updateRequest.setAge(21);

        mockMvc.perform(put("/api/v1/users/{id}", saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Новое имя"))
                .andExpect(jsonPath("$.email").value("new@mail.com"))
                .andExpect(jsonPath("$.age").value(21));
    }

    @Test
    void deleteUser_ShouldReturnNoContent() throws Exception {
        User user = new User();
        user.setName("Удаляемый");
        user.setEmail("delete@mail.com");
        user.setAge(40);
        User saved = userRepository.save(user);

        mockMvc.perform(delete("/api/v1/users/{id}", saved.getId()))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/{id}", saved.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllUsers_ShouldReturnList() throws Exception {
        User user1 = new User(); user1.setName("A"); user1.setEmail("a@a.com"); user1.setAge(1);
        User user2 = new User(); user2.setName("B"); user2.setEmail("b@b.com"); user2.setAge(2);
        userRepository.saveAll(List.of(user1, user2));

        mockMvc.perform(get("/api/v1/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", in(List.of("A", "B"))));
    }

    @Test
    void createUser_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        UserRequest invalid = new UserRequest();
        invalid.setName("");
        invalid.setEmail("not-an-email");
        invalid.setAge(150);

        mockMvc.perform(post("/api/v1/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.email").exists())
                .andExpect(jsonPath("$.age").exists());
    }
}