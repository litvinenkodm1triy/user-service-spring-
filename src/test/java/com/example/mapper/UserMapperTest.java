package com.example.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import com.example.dto.request.UserRequest;
import com.example.dto.response.UserResponse;
import com.example.entity.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void toEntity_ShouldMapRequestToEntity() {
        UserRequest request = new UserRequest();
        request.setName("Анна");
        request.setEmail("anna@mail.com");
        request.setAge(28);

        User user = mapper.toEntity(request);

        assertThat(user.getId()).isNull();
        assertThat(user.getName()).isEqualTo("Анна");
        assertThat(user.getEmail()).isEqualTo("anna@mail.com");
        assertThat(user.getAge()).isEqualTo(28);
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
    }

    @Test
    void toResponse_ShouldMapEntityToResponse() {
        User user = new User();
        user.setId(5L);
        user.setName("Борис");
        user.setEmail("boris@mail.com");
        user.setAge(35);
        user.setCreatedAt(LocalDateTime.of(2024, 1, 1, 12, 0));
        user.setUpdatedAt(LocalDateTime.of(2024, 1, 2, 13, 0));

        UserResponse response = mapper.toResponse(user);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getName()).isEqualTo("Борис");
        assertThat(response.getEmail()).isEqualTo("boris@mail.com");
        assertThat(response.getAge()).isEqualTo(35);
        assertThat(response.getCreatedAt()).isEqualTo(user.getCreatedAt());
        assertThat(response.getUpdatedAt()).isEqualTo(user.getUpdatedAt());
    }

    @Test
    void updateUserFromRequest_ShouldUpdateExistingEntity() {
        User existingUser = new User();
        existingUser.setId(10L);
        existingUser.setName("Старое имя");
        existingUser.setEmail("old@mail.com");
        existingUser.setAge(20);
        existingUser.setCreatedAt(LocalDateTime.now().minusDays(1));
        existingUser.setUpdatedAt(LocalDateTime.now().minusDays(1));

        UserRequest request = new UserRequest();
        request.setName("Новое имя");
        request.setEmail("new@mail.com");
        request.setAge(25);

        mapper.updateUserFromRequest(request, existingUser);

        assertThat(existingUser.getId()).isEqualTo(10L);
        assertThat(existingUser.getName()).isEqualTo("Новое имя");
        assertThat(existingUser.getEmail()).isEqualTo("new@mail.com");
        assertThat(existingUser.getAge()).isEqualTo(25);
        assertThat(existingUser.getCreatedAt()).isNotNull();
        assertThat(existingUser.getUpdatedAt()).isNotNull();
    }
}