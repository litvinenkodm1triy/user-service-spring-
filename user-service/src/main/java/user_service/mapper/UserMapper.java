package user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import user_service.dto.request.UserRequest;
import user_service.dto.response.UserCreateResponse;
import user_service.dto.response.UserUpdateResponse;
import user_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest userRequest);

    UserCreateResponse toUserCreateResponse(User user);

    UserUpdateResponse toUserUpdateResponse(User user);
}
