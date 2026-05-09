package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.dto.request.UserRequest;
import com.example.dto.response.UserResponse;
import com.example.entity.User;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserRequest request;
    private User user;
    private UserResponse response;

    @BeforeEach
    void setUp() {
        request = new UserRequest();
        request.setName("Тест");
        request.setEmail("test@example.com");
        request.setAge(25);

        user = new User();
        user.setId(1L);
        user.setName("Тест");
        user.setEmail("test@example.com");
        user.setAge(25);

        response = new UserResponse();
        response.setId(1L);
        response.setName("Тест");
        response.setEmail("test@example.com");
        response.setAge(25);
    }

    @Test
    void createUser_ShouldReturnUserResponse() {
        when(userMapper.toEntity(request)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.createUser(request);

        assertThat(result).isEqualTo(response);
        verify(userRepository).save(user);
    }

    @Test
    void findUserById_WhenExists_ShouldReturnResponse() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        UserResponse result = userService.findUserById(1L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void findUserById_WhenNotExists_ShouldThrowUserNotFoundException() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.findUserById(99L))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Не найден пользователь с id: 99");
    }

    @Test
    void updateUser_ShouldUpdateAndReturnResponse() {
        UserRequest updateRequest = new UserRequest();
        updateRequest.setName("Обновлённый");
        updateRequest.setEmail("updated@example.com");
        updateRequest.setAge(30);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        doNothing().when(userMapper).updateUserFromRequest(updateRequest, user);
        when(userRepository.save(user)).thenReturn(user);

        UserResponse updatedResponse = new UserResponse();
        updatedResponse.setId(1L);
        updatedResponse.setName("Обновлённый");
        updatedResponse.setEmail("updated@example.com");
        updatedResponse.setAge(30);
        when(userMapper.toResponse(user)).thenReturn(updatedResponse);

        UserResponse result = userService.updateUser(1L, updateRequest);

        assertThat(result.getName()).isEqualTo("Обновлённый");
        verify(userMapper).updateUserFromRequest(updateRequest, user);
        verify(userRepository).save(user);
    }

    @Test
    void deleteUserById_WhenExists_ShouldDelete() {
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        userService.deleteUserById(1L);

        verify(userRepository).deleteById(1L);
    }

    @Test
    void deleteUserById_WhenNotExists_ShouldThrowException() {
        when(userRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> userService.deleteUserById(99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void findAllUsers_ShouldReturnListOfResponses() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponse(user)).thenReturn(response);

        List<UserResponse> results = userService.findAllUsers();

        assertThat(results).hasSize(1);
        assertThat(results.get(0)).isEqualTo(response);
    }
}