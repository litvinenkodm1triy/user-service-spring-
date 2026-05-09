package user_service.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRequest {

    @NotBlank(message = "Имя не может быть пустым")
    @Size(max = 100, message = "Максимальное значение символов в имени не должно превышать 100")
    private String name;

    @NotBlank(message = "Email не может быть пустым")
    @Email(message = "Email должен быть корректным (содержать @ и домен)")
    private String email;

    @Min(value = 0, message = "Возраст должен быть не меньше 0")
    @Max(value = 120, message = "Возраст должен быть не больше 120")
    private Integer age;
}
