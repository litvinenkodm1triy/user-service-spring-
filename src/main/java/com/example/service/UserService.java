package com.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.dto.request.UserRequest;
import com.example.dto.response.UserResponse;
import com.example.entity.User;
import com.example.exception.UserNotFoundException;
import com.example.mapper.UserMapper;
import com.example.repository.UserRepository;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserEventPublisher eventPublisher;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        log.debug("Преобразование UserRequest в Entity: {}", request);
        User user = userMapper.toEntity(request);
        User saved = userRepository.save(user);
        log.info("Пользователь сохранён с id={}", saved.getId());
        eventPublisher.publishUserEvent("CREATE", saved.getEmail());
        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        log.debug("Поиск пользователя по id={}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь с id={} не найден в БД", id);
                    return new UserNotFoundException("Не найден пользователь с id: " + id);
                });
        log.debug("Пользователь найден: {}", user);
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        log.debug("Обновление пользователя id={}, данные: {}", id, request);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Пользователь id={} не найден для обновления", id);
                    return new UserNotFoundException("Не найден пользователь с id: " + id);
                });
        userMapper.updateUserFromRequest(request, user);
        User updated = userRepository.save(user);
        log.info("Пользователь id={} обновлён", id);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public void deleteUserById(Long id) {
        log.debug("Проверка существования пользователя id={} для удаления", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Попытка удалить несуществующего пользователя id={}", id);
                    return new UserNotFoundException("Не найден пользователь с id: " + id);
                });
        String email = user.getEmail();
        userRepository.deleteById(id);
        log.info("Пользователь id={} удалён из БД", id);
        eventPublisher.publishUserEvent("DELETE", email);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        log.debug("Запрос всех пользователей из репозитория");
        List<UserResponse> users = userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
        log.debug("Загружено {} пользователей", users.size());
        return users;
    }
}