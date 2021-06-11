package tacos.email;

import org.springframework.integration.handler.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

// This is a message handler: posts the order to Taco Cloud's API
@Component
public class OrderSubmitMessageHandler implements GenericHandler<Order> {

    private RestTemplate restTemplate;
    private ApiProperties apiProperties;

    public OrderSubmitMessageHandler(RestTemplate restTemplate, ApiProperties apiProperties) {
        this.restTemplate = restTemplate;
        this.apiProperties = apiProperties;
    }

    @Override
    public Object handle(Order order, MessageHeaders messageHeaders) {
        restTemplate.postForObject(apiProperties.getURL(), order, String.class);
        return null;
    }
    // postForObject() = posts data to the URL and returns the object created
}

// handle() receives the incoming Order object and uses an injected RestTemplate to submit the Order via a POST request to the URL captured in an injected ApiProperties object
// handle() returns null to indicate that this handler is the end of the flow