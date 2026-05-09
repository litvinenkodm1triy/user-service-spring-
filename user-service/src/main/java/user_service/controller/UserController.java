package user_service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import user_service.dto.request.UserRequest;
import user_service.dto.response.UserResponse;
import user_service.service.UserService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserRequest request) {
        log.info("==> Создание пользователя: {}", request);
        UserResponse response = userService.createUser(request);
        log.info("<== Пользователь создан, id={}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id) {
        log.info("==> Запрос пользователя с id={}", id);
        UserResponse response = userService.findUserById(id);
        log.info("<== Найден пользователь: {}", response);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequest request) {
        log.info("==> Обновление пользователя id={}, данные: {}", id, request);
        UserResponse response = userService.updateUser(id, request);
        log.info("<== Пользователь id={} обновлён", id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("==> Запрос на удаление пользователя id={}", id);
        userService.deleteUserById(id);
        log.info("<== Пользователь id={} удалён", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        log.info("==> Запрос всех пользователей");
        List<UserResponse> users = userService.findAllUsers();
        log.info("<== Найдено {} пользователей", users.size());
        return ResponseEntity.ok(users);
    }
}