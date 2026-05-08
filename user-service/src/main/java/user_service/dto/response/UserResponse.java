package user_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private Integer age;

    private LocalDateTime createdAt;

    private LocalDateTime updateAt;

}
