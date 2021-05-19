package tacos.messaging;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import tacos.Order;

import java.util.Collection;
import java.util.Map;

@Service
public class RabbitMQOrderMessagingService implements OrderMessagingService {

    private RabbitTemplate rabbitTemplate;
    private MessageConverter messageConverter;

    @Bean
    public RabbitAdmin rabbitAdmin() {
        RabbitAdmin admin = new RabbitAdmin(rabbitTemplate);
        Queue queue = new Queue("tacocloud.order.queue");
        admin.declareQueue(queue);
        TopicExchange topicExchange = new TopicExchange("tacocloud.orders");
        admin.declareExchange(topicExchange);
        Binding binding = BindingBuilder.bind(queue).to(topicExchange).with("tacocloud.order.queue");
        admin.declareBinding(binding);
        System.out.println("S_A EXECUTAT!");
        return admin;
    }
    // this bean creates a queue, an exchange and a binding

    @Autowired
    public RabbitMQOrderMessagingService(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageConverter = rabbitTemplate.getMessageConverter();
    }

    // please uncomment what method you want to use

    // ----------------------------------- raw messages -----------------------------------

    //  ======================== 1. method send(Message) ========================
    @Override
    public void sendOrder(Order order) {
        MessageProperties properties = new MessageProperties();
        Message message = messageConverter.toMessage(order, properties);
        rabbitTemplate.send(message);
        System.out.println("Convert and sent order");
    }

    //  ======================== 2. method send(String routingKey, Message) ========================
    /*@Override
    public void sendOrder(Order order) {
        MessageProperties properties = new MessageProperties();
        Message message = messageConverter.toMessage(order, properties);
        rabbitTemplate.send("tacocloud.order.queue", message);
        System.out.println("Convert and sent order");
    }*/

    //  ======================== 3. method send(String exchange, String routingKey, Message) ========================
    /*@Override
    public void sendOrder(Order order) {
        MessageProperties properties = new MessageProperties();
        Message message = messageConverter.toMessage(order, properties);
        rabbitTemplate.send("tacocloud.orders","tacocloud.order.queue", message);
        System.out.println("Convert and sent order");
    }*/

    // ----------------------------------- converting messages before sending -----------------------------------
    // use convertAndSend() to let RabbitTemplate handle all of the conversion work for you

    //  ======================== 4. method covertAndSend(Object message) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(order);
        System.out.println("Convert and sent order");
    }*/

    //  ======================== 5. method covertAndSend(String routingKey, Object message) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.order.queue", order);
        System.out.println("Convert and sent order");
    }*/

    //  ======================== 6. method covertAndSend(String exchange, String routingKey, Object message) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.orders", "tacocloud.order.queue", order);
        System.out.println("Convert and sent order");
    }*/


    // ----------------------------------- post-processing messages -----------------------------------

    //  ======================== 7. method covertAndSend(Object message, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(order, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                MessageProperties properties = message.getMessageProperties();
                properties.setHeader("X_ORDER_SOURCE", "WEB");
                return message;
            }
        });
    }*/
    // MessagePostProcessor is a functional interface - so we can change the sendOrder() method to use lambda
    // overridden with lambda

    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(order, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/

    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend(order, this::addOrderSource);
        System.out.println("Convert and sent order");
    }*/

    //  ======================== 8. method covertAndSend(String routingKey, Object message, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.order.queue", order, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/

    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);
        System.out.println("Convert and sent order");
    }*/

    //  ======================== 9. method covertAndSend(String exchange, String routingKey, Object message, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.orders", "tacocloud.order.queue", order, message -> {
            MessageProperties properties = message.getMessageProperties();
            properties.setHeader("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/

    // we replace this code with a method reference instead of a lambda (to avoid code duplication)

    /*@Override
    public void sendOrder(Order order) {
        rabbitTemplate.convertAndSend("tacocloud.orders", "tacocloud.order.queue", order, this::addOrderSource);
        System.out.println("Convert and sent order");
    }*/

    private Message addOrderSource(Message message) {
        MessageProperties properties = message.getMessageProperties();
        properties.setHeader("X_ORDER_SOURCE", "WEB");
        return message;
    }
}
