package banquemisr.challenge05.taskManagement.model.dto;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskHistoryDto implements Serializable {

    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private User createdBy;
    private Date createdAt;
    @JsonIgnoreProperties({"user", "taskHistories"})
    private Task task;
    @JsonIgnoreProperties({"tasks"})
    private UserDto user;
}
