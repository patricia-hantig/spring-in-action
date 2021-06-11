package tacos.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

// ApiProperties is used to avoid hardcoding the URL in the call to postForObject() from OrderSubmitMessageHandler class

@Data
@ConfigurationProperties(prefix = "tacocloud.api")
@Component
public class ApiProperties {
    private String URL;
}
