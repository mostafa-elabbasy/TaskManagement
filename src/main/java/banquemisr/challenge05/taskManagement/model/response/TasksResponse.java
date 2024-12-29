package banquemisr.challenge05.taskManagement.model.response;

import banquemisr.challenge05.taskManagement.model.dto.TaskDto;
import banquemisr.challenge05.taskManagement.model.dto.UserDto;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TasksResponse {
    private Long totalItems;
    private Integer totalPages;
    private List<TaskDetails> tasks;

    @Getter
    @Setter
    public static class TaskDetails {
        private UUID id;
        private String title;
        private String description;
        private Status status;
        private Priority priority;
        private LocalDate dueDate;
        @JsonIgnoreProperties({"tasks"})
        private UserDto user;
    }
}
