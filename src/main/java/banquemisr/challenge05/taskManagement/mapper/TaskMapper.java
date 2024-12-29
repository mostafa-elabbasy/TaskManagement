package banquemisr.challenge05.taskManagement.mapper;

import banquemisr.challenge05.taskManagement.model.dto.TaskDto;
import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.entity.TaskHistory;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.request.TaskRequest;
import banquemisr.challenge05.taskManagement.model.response.TasksResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    Task toEntity(TaskDto taskDto);

    Task requestToEntity(TaskRequest taskRequest);

    TaskDto toDto(Task task);

    List<TaskDto> toDtoList(List<Task> tasks);

    @Mapping(target = "tasks" , ignore = true)
    UserDto toUserDto(User user);

    TasksResponse.TaskDetails toTaskDetails(Task task);

    List<TasksResponse.TaskDetails> toTaskDetailsList(List<Task> tasks);

    TaskHistory toTaskHistory(Task task);

}
