package banquemisr.challenge05.taskManagement.service.notification;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import banquemisr.challenge05.taskManagement.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSenderScheduler {

    private final TaskRepository taskRepository;
    private final NotificationService notificationService;

    @Scheduled(cron = "0 0 6 * * ?")
    public void sendNotification() {
        List<Task> tasks = taskRepository.findByDueDateBetweenAndStatusNot(LocalDate.now(), LocalDate.now().plusDays(3), Status.DONE);
        tasks.forEach(task -> {
            notificationService.sendTaskDueDateNotification(task);
        });
    }



}
