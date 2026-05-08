package user_service.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user_service.dto.request.UserRequest;
import user_service.dto.response.UserResponse;
import user_service.entity.User;
import user_service.exception.UserNotFoundException;
import user_service.mapper.UserMapper;
import user_service.repository.UserRepository;
import user_service.validation.UserValidation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse createUser(UserRequest request) {
        User user = userMapper.toEntity(request);
        UserValidation.nameValidation(user);
        UserValidation.emailValidation(user);
        UserValidation.ageValidation(user);

        User saved = userRepository.save(user);
        return userMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponse findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: " + id));
        return userMapper.toResponse(user);
    }

    @Transactional
    public UserResponse updateUser(Long id, UserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("Не найден пользователь с id: " + id));

        userMapper.updateUserFromRequest(request, user);
        // ✅ валидация обновлённых данных
        UserValidation.nameValidation(user);
        UserValidation.emailValidation(user);
        UserValidation.ageValidation(user);

        User updated = userRepository.save(user);
        return userMapper.toResponse(updated);
    }

    @Transactional
    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("Не найден пользователь с id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }
}