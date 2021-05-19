package tacos.kitchen.messaging.rabbit;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import tacos.Order;
import tacos.kitchen.OrderReceiver;

// Receiving messages with RabbitTemplate (pull model)
@Profile("rabbitmq-template")
@Component("templateOrderReceiver")
public class RabbitOrderReceiver implements OrderReceiver {

    private RabbitTemplate rabbitTemplate;
    private MessageConverter messageConverter;

    @Autowired
    public RabbitOrderReceiver(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = rabbitTemplate.getMessageConverter();

        // this specification is needed to have a default queue specified for receive() methods that don't specify the queue
        this.rabbitTemplate.setDefaultReceiveQueue("tacocloud.order.queue");
    }

    // used when we work with raw messages

    // please uncomment what method you want to use

    // ----------------------------------- raw messages -----------------------------------

    //  ======================== 1. method receive() ========================
    @Override
    public Order receiveOrder() {
        Message message = rabbitTemplate.receive();
        return message != null ? (Order) messageConverter.fromMessage(message) : null;
    }

    //  ======================== 2. method receive(String queueName) ========================
    /*@Override
    public Order receiveOrder() {
        Message message = rabbitTemplate.receive("tacocloud.order.queue");
        return message != null ? (Order) messageConverter.fromMessage(message) : null;
    }*/

    //  ======================== 3. method receive(long timeoutMillis) ========================
    // let's say we want to wait 3 seconds before giving up if there is no message
    /*@Override
    public Order receiveOrder() {
        Message message = rabbitTemplate.receive(3000);
        return message != null ? (Order) messageConverter.fromMessage(message) : null;
    }*/

    //  ======================== 4. method receive(String queueName, long timeoutMillis) ========================
    /*@Override
    public Order receiveOrder() {
        Message message = rabbitTemplate.receive("tacocloud.order.queue", 30000);
        return message != null ? (Order) messageConverter.fromMessage(message) : null;
    }*/
    // if you don't want to have hardcoded numbers -> you can remove the timeout value and call receive() & set the value for timeout in the configuration file


    // ----------------------------------- messages converted to objects  -----------------------------------

    //  ======================== 5. method receiveAndConvert() ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert();
    }*/

    //  ======================== 6. method receiveAndConvert(String queueName) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert("tacocloud.order.queue");
    }*/

    //  ======================== 7. method receiveAndConvert(long timeoutMillis) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert(30000);
    }*/

    //  ======================== 8. method receiveAndConvert(String queueName, long timeoutMillis) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert("tacocloud.order.queue", 30000);
    }*/


    // ----------------------------------- messages converted to type-safe objects  -----------------------------------
    // an alternative for casting to Order
    // the requirement for this is that the message converter must be an implementation of SmartMessageConverter - you can use the out-of-the-box implementation: Jackson2JsonMessageConverter

    //  ======================== 9. method receiveAndConvert(ParameterizedTypeReference) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert(new ParameterizedTypeReference<Order>() {
        });
    }*/

    //  ======================== 10. method receiveAndConvert(String queueName, ParameterizedTypeReference) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert("tacocloud.order.queue", new ParameterizedTypeReference<Order>() {
        });
    }*/

    //  ======================== 11. method receiveAndConvert(long timeoutMillis, ParameterizedTypeReference) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert(3000, new ParameterizedTypeReference<Order>() {
        });
    }*/

    //  ======================== 12. method receiveAndConvert(String queueName, long timeoutMillis, ParameterizedTypeReference) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) rabbitTemplate.receiveAndConvert("tacocloud.order.queue", 3000, new ParameterizedTypeReference<Order>() {
        });
    }*/

}
