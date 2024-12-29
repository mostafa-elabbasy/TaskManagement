package banquemisr.challenge05.taskManagement.repository;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID>, JpaSpecificationExecutor<Task> {

    List<Task> findByDueDateBetweenAndStatusNot(LocalDate startDate, LocalDate endDate, Status status);

}
