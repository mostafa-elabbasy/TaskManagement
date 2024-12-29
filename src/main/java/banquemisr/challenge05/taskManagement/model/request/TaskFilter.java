package banquemisr.challenge05.taskManagement.model.request;

import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskFilter implements Serializable {
    private UUID id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate dueDate;
    private UUID userId;
    private int offset;
    private int limit;
    private String sortBy = "id";
    private String sortDirection = "DESC";
}
