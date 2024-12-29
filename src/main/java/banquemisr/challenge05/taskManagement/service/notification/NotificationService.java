package banquemisr.challenge05.taskManagement.service.notification;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.request.EmailRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationSenderService notificationSenderService;

    public void sendTaskDueDateNotification(Task task) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(task.getUser().getEmail());
        emailRequest.setSubject("Task Notification");
        emailRequest.setTemplate("task-due-date-notification.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", task.getUser().getFullName());
        properties.put("taskTitle", task.getTitle());
        properties.put("taskDueDate", task.getDueDate());

        emailRequest.setProperties(properties);

        notificationSenderService.sendHtmlMessage(emailRequest);

    }

    @Async
    public void sendCreateTaskNotification(Task task) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(task.getUser().getEmail());
        emailRequest.setSubject("Task Notification");
        emailRequest.setTemplate("new-task-notification.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", task.getUser().getFullName());
        properties.put("taskTitle", task.getTitle());

        emailRequest.setProperties(properties);

        notificationSenderService.sendHtmlMessage(emailRequest);

    }

    @Async
    public void sendUpdateTaskNotification(Task task) {
        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setTo(task.getUser().getEmail());
        emailRequest.setSubject("Task Notification");
        emailRequest.setTemplate("update-task-notification.html");

        Map<String, Object> properties = new HashMap<>();
        properties.put("userName", task.getUser().getFullName());
        properties.put("taskTitle", task.getTitle());

        emailRequest.setProperties(properties);

        notificationSenderService.sendHtmlMessage(emailRequest);

    }

}
