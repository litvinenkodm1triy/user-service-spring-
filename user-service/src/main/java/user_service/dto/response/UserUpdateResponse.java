package user_service.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserUpdateResponse {

    private Long id;

    private String email;

    private String name;

    private Integer age;

    private LocalDateTime updatedAt;
}
