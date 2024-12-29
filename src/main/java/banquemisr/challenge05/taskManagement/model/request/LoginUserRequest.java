package banquemisr.challenge05.taskManagement.model.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class LoginUserRequest implements Serializable {
  private String email;
  private String password;
}
