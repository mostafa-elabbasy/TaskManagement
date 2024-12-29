package banquemisr.challenge05.taskManagement.model.request;

import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private UUID id;
    @NotNull(message = "title is required")
    @NotEmpty(message = "title is required")
    @Size(max = 255, message = "title max size is 255")
    private String title;
    @Size(max = 1000, message = "description max size is 1000")
    private String description;
    @NotNull(message = "status is required")
    private Status status;
    @NotNull(message = "priority is required")
    private Priority priority;
    @NotNull(message = "due date is required")
    private LocalDate dueDate;
    @NotNull(message = "user is required")
    private UUID userId;

}
