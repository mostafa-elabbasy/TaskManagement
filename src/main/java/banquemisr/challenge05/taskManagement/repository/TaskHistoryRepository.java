package banquemisr.challenge05.taskManagement.repository;

import banquemisr.challenge05.taskManagement.model.entity.TaskHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TaskHistoryRepository extends JpaRepository<TaskHistory, UUID> {
}
