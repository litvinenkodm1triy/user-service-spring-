package user_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserCreateResponse {

    private Long id;

    private String name;

    private String email;

    private Integer age;

    private LocalDateTime createdAt;

}
