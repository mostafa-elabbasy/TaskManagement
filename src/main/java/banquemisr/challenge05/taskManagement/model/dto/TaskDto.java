package banquemisr.challenge05.taskManagement.model.dto;

import banquemisr.challenge05.taskManagement.model.entity.TaskHistory;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto implements Serializable {

    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    @JsonIgnoreProperties({"tasks"})
    private UserDto user;
    @JsonIgnoreProperties({"task","user","createdBy"})
    private List<TaskHistoryDto> taskHistories;
}
