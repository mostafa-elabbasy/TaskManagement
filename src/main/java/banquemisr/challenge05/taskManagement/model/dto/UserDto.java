package banquemisr.challenge05.taskManagement.model.dto;

import banquemisr.challenge05.taskManagement.model.entity.Task;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private Date createdAt;
    private Date updatedAt;
    private Set<RoleDto> authorities;
    @JsonIgnoreProperties("user")
    private Set<TaskDto> tasks;
}
