package user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import user_service.dto.request.UserRequest;
import user_service.dto.response.UserResponse;
import user_service.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRequest userRequest);


    @Mapping(source = "updatedAt", target = "updatedAt")
    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateUserFromRequest(UserRequest request, @MappingTarget User user);
}