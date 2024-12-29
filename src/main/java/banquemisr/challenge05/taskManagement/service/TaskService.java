package banquemisr.challenge05.taskManagement.service;

import banquemisr.challenge05.taskManagement.exception.BusinessException;
import banquemisr.challenge05.taskManagement.mapper.TaskMapper;
import banquemisr.challenge05.taskManagement.model.dto.TaskDto;
import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.entity.TaskHistory;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.request.TaskFilter;
import banquemisr.challenge05.taskManagement.model.request.TaskRequest;
import banquemisr.challenge05.taskManagement.model.response.TasksResponse;
import banquemisr.challenge05.taskManagement.model.specification.TaskSpecification;
import banquemisr.challenge05.taskManagement.repository.TaskHistoryRepository;
import banquemisr.challenge05.taskManagement.repository.TaskRepository;
import banquemisr.challenge05.taskManagement.repository.UserRepository;
import banquemisr.challenge05.taskManagement.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final TaskHistoryRepository taskHistoryRepository;

    public TaskDto createTask(TaskRequest taskRequest, String email) {
        User user = userRepository.findById(taskRequest.getUserId()).orElseThrow(() -> new BusinessException("user not found!"));
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("user not found!"));
        Task task = taskMapper.requestToEntity(taskRequest);
        task.setUser(user);
        Task save = taskRepository.save(task);
        notificationService.sendCreateTaskNotification(save);

        TaskHistory taskHistory = taskMapper.toTaskHistory(task);
        taskHistory.setId(null);
        taskHistory.setCreatedBy(currentUser);
        taskHistory.setTask(save);
        taskHistoryRepository.save(taskHistory);

        return taskMapper.toDto(save);
    }

    public TaskDto updateTask(TaskRequest taskRequest, String email) {
        taskRepository.findById(taskRequest.getId()).orElseThrow(() -> new BusinessException("task not found"));
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("user not found!"));
        User user = userRepository.findById(taskRequest.getUserId()).orElseThrow(() -> new BusinessException("user not found!"));
        Task task = taskMapper.requestToEntity(taskRequest);
        task.setUser(user);
        Task save = taskRepository.save(task);
        notificationService.sendUpdateTaskNotification(task);

        TaskHistory taskHistory = taskMapper.toTaskHistory(task);
        taskHistory.setId(null);
        taskHistory.setCreatedBy(currentUser);
        taskHistory.setTask(save);
        taskHistoryRepository.save(taskHistory);

        return taskMapper.toDto(save);
    }

    public TaskDto findTask(UUID id, String email) {
        User currentUser = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("user not found!"));
        boolean isAdmin = currentUser.getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).collect(Collectors.toList()).size() > 0;
        Task task = taskRepository.findById(id).orElseThrow(() -> new BusinessException("task not found"));
        if (!isAdmin && !task.getUser().getId().equals(currentUser.getId()))
            throw new BusinessException("you are not authorize for this task");

        return taskMapper.toDto(task);
    }

    public void deleteTask(UUID id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new BusinessException("task not found"));
        taskRepository.delete(task);
    }

    public TasksResponse search(TaskFilter taskFilter, String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new BusinessException("user not found!"));
        boolean isAdmin = user.getAuthorities().stream().filter(role -> role.getAuthority().equals("ROLE_ADMIN")).collect(Collectors.toList()).size() > 0;
        if (!isAdmin) {
            taskFilter.setUserId(user.getId());
        }
        Specification<Task> specification = buildSpecifications(taskFilter);
        PageRequest pageRequest = buildPagination(taskFilter);
        Page<Task> tasks = taskRepository.findAll(specification, pageRequest);
        List<TasksResponse.TaskDetails> taskDetailsList = taskMapper.toTaskDetailsList(tasks.getContent());

        TasksResponse tasksResponse = new TasksResponse();
        tasksResponse.setTasks(taskDetailsList);
        tasksResponse.setTotalPages(tasks.getTotalPages());
        tasksResponse.setTotalItems(tasks.getTotalElements());

        return tasksResponse;
    }

    private Specification<Task> buildSpecifications(TaskFilter filter) {
        return Specification.where(TaskSpecification.id(filter.getId())
                .and(TaskSpecification.title(filter.getTitle()))
                .and(TaskSpecification.status(filter.getStatus()))
                .and(TaskSpecification.description(filter.getDescription()))
                .and(TaskSpecification.priority(filter.getPriority()))
                .and(TaskSpecification.dueDate(filter.getDueDate()))
                .and(TaskSpecification.userId(filter.getUserId())));
    }

    private PageRequest buildPagination(TaskFilter filter) {
        return PageRequest.of(filter.getOffset(), filter.getLimit(), Sort.by(Sort.Direction.valueOf(filter.getSortDirection()), filter.getSortBy()));
    }

}
