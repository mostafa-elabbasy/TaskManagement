package banquemisr.challenge05.taskManagement.controller;

import banquemisr.challenge05.taskManagement.model.dto.TaskDto;
import banquemisr.challenge05.taskManagement.model.request.TaskFilter;
import banquemisr.challenge05.taskManagement.model.request.TaskRequest;
import banquemisr.challenge05.taskManagement.model.response.TasksResponse;
import banquemisr.challenge05.taskManagement.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.UUID;

@RequestMapping("/api/v1/task")
@RestController
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody @Valid TaskRequest taskRequest, Principal principal) {
        return new ResponseEntity<>(taskService.createTask(taskRequest, principal.getName()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @PutMapping
    public ResponseEntity<TaskDto> update(@RequestBody @Valid TaskRequest taskRequest, Principal principal) {
        return new ResponseEntity<>(taskService.updateTask(taskRequest, principal.getName()), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskDto> getTask(@PathVariable UUID id, Principal principal) {
        return new ResponseEntity<>(taskService.findTask(id, principal.getName()), HttpStatus.OK);
    }

    @PostMapping("/list")
    public ResponseEntity<TasksResponse> search(@RequestBody TaskFilter taskFilter, Principal principal){
        return new ResponseEntity<>(taskService.search(taskFilter, principal.getName()) , HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN_ROLE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable UUID id) {
        taskService.deleteTask(id);

        return new ResponseEntity<>("deleted successfully", HttpStatus.NO_CONTENT);
    }

}
