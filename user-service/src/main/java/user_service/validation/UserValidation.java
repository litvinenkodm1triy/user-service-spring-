package user_service.validation;

import user_service.entity.User;
import user_service.exception.UserValidationException;

public class UserValidation {

    public static void ageValidation(User user) {
        Integer age = user.getAge();
        if (age != null && (age < 0 || age > 120)) {
            throw new UserValidationException("Значение возраста должно быть в пределах от 0 до 120!");
        }
    }

    public static void nameValidation(User user) {
        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            throw new UserValidationException("Имя не может быть пустым!");
        }
        if (name.length() > 100) {
            throw new UserValidationException("Максимальное значение символов в имени не должно превышать 100!");
        }
    }

    public static void emailValidation(User user) {
        String email = user.getEmail();
        if (email == null || email.trim().isEmpty()) {
            throw new UserValidationException("не может быть пустым");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new UserValidationException("Email должен содержать @ и .");
        }
    }
}