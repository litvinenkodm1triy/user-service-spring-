package user_service.dto.request;

import lombok.Data;

@Data
public class UserRequest {

    private String name;

    private String email;

    private Integer age;
}
