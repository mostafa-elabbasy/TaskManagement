package banquemisr.challenge05.taskManagement.mapper;

import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "tasks", source = "tasks", ignore = true)
    UserDto toDto(User user);
    @Mapping(target = "tasks", source = "tasks", ignore = true)
    User toUser(UserDto userDto);
    List<UserDto> toDtoList(List<User> users);

}
