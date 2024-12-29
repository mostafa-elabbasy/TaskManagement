package banquemisr.challenge05.taskManagement.model.specification;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import banquemisr.challenge05.taskManagement.model.entity.User;
import banquemisr.challenge05.taskManagement.model.enums.Priority;
import banquemisr.challenge05.taskManagement.model.enums.Status;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class TaskSpecification {

    public static Specification<Task> id(UUID id) {

        return (root, query, builder) -> id == null ? builder.conjunction() : builder.equal(root.get("id"), id);
    }

    public static Specification<Task> title(String title) {

        return (root, query, builder) -> title == null || title.isEmpty() ? builder.conjunction() : builder.equal(root.get("title"), title);
    }

    public static Specification<Task> description(String description) {

        return (root, query, builder) -> description == null || description.isEmpty() ? builder.conjunction() : builder.equal(root.get("description"), description);
    }

    public static Specification<Task> status(Status status) {

        return (root, query, builder) -> status == null ? builder.conjunction() : builder.equal(root.get("status"), status);
    }

    public static Specification<Task> priority(Priority priority) {

        return (root, query, builder) -> priority == null ? builder.conjunction() : builder.equal(root.get("priority"), priority);
    }

    public static Specification<Task> dueDate(LocalDate dueDate) {

        return (root, query, builder) -> dueDate == null ? builder.conjunction() : builder.equal(root.get("dueDate"), dueDate);
    }

    public static Specification<Task> userId(UUID userId) {

        return (root, query, builder) -> {
            if (Objects.isNull(userId))
                return builder.conjunction();
            else {
                query.distinct(true);
                Join<Task, User> taskUserJoin = root.join("user", JoinType.LEFT);
                return builder.equal(taskUserJoin.get("id"), userId);
            }
        };
    }

}
