package banquemisr.challenge05.taskManagement.model.entity;

import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "tasks")
@Entity
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", length = 1000)
    private String description;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 100)
    private Status status;
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false, length = 100)
    private Priority priority;
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    @JsonIgnoreProperties("tasks")
    private User user;
    @OneToMany(mappedBy = "task", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<TaskHistory> taskHistories;

}
