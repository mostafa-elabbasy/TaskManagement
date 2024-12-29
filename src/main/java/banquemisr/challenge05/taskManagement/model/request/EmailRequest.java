package banquemisr.challenge05.taskManagement.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest implements Serializable {
    private String to;
    private String subject;
    private String template;
    private Map<String , Object> properties;
}
