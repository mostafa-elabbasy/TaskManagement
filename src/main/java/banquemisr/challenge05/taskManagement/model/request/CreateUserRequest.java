package banquemisr.challenge05.taskManagement.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CreateUserRequest implements Serializable {
  private String email;

  private String password;

  private String fullName;
  private List<String> authorities;
}
