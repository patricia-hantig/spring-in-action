package tacos.messaging;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.stereotype.Service;
import tacos.Order;

import javax.jms.*;

// Sending messages with JmsTemplate
@Service
public class JmsOrderMessagingService implements OrderMessagingService {

    private JmsTemplate jms;
    private Destination orderQueue;

    @Autowired
    public JmsOrderMessagingService(JmsTemplate jms, Destination orderQueue) {
        this.jms = jms;
        this.orderQueue = orderQueue;
    }

    // please uncomment what method you want to use

    // ----------------------------------- raw messages -----------------------------------
    // make sure to use a receiveOrder() method that also works with raw messages & that the domain classes that you work with are serializable (on send & receive side)
    // domain classes are - Ingredient.java, Taco.java, Order.java

    //  ======================== 1. method send(MessageCreator) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(order);
            }
        });
    }*/
    // jms.send() - pass in an anonymous inner-class implementation of MessageCreator
    // createMessage() = creates a new object message from the given Order object
    // MessageCreator is a functional interface - so we can change the sendOrder() method to use lambda
    // overridden with lambda

    /*@Override
    public void sendOrder(Order order) {
        jms.send(session -> session.createObjectMessage(order));
    }*/
    // this method jms.send() does not specify a destination => so we have to add a default destination name property in the application.yml file


    //  ======================== 2. method send(Destination, MessageCreator) ========================
    // you have to declare a Destination bean first & then inject it into the bean that performs messaging

    // the instantiation is done in the constructor of JmsOrderMessagingService
    // now you can use Destination to specify the destination when calling send()
    /*@Override
    public void sendOrder(Order order) {
        jms.send(orderQueue, session -> session.createObjectMessage(order));
    }*/
    // using Destination object allows you configure Destination with more than just the destination name


    //  ======================== 3. method send(String, MessageCreator) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.send("tacocloud.order.queue", session -> session.createObjectMessage(order));
    }*/
    // specify the destination name only as String -> you’ll almost never specify anything more than the destination name


    // ----------------------------------- converting messages before sending -----------------------------------

    //  ======================== 4. method convertAndSend(Object) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend(order);
    }*/
    // we don't provide a MessageCreator - we just pass the object that we want to send & it wil be converted into a Message before it's sent
    // this method will use the default destination


    //  ======================== 5. method convertAndSend(Destination, Object) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend(orderQueue, order);
    }*/


    //  ======================== 6. method convertAndSend(String, Object) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend("tacocloud.order.queue", order);
    }*/


    // ----------------------------------- post-processing messages -----------------------------------

    //  ======================== 7. method send(String, MessageCreator) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.send("tacocloud.order.queue", session -> {
            Message message = session.createObjectMessage(order);
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/
    // we add setStringProperty() to change the Message

    //  ======================== 8. method convertAndSend(Object, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend(order, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("X_ORDER_SOURCE", "WEB");
                return message;
            }
        });
    }*/
    // MessagePostProcessor is a functional interface - so we can change the sendOrder() method to use lambda
    // overridden with lambda

    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend(order, message -> {
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/

    //  ======================== 9. method convertAndSend(Destination, Object, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend(orderQueue, order, message -> {
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/

    //  ======================== 10. method convertAndSend(String, Object, MessagePostProcessor) ========================
    /*@Override
    public void sendOrder(Order order) {
        jms.convertAndSend("tacocloud.order.queue", order, message -> {
            message.setStringProperty("X_ORDER_SOURCE", "WEB");
            return message;
        });
    }*/
    // although we need this particular MessagePostProcessor for this one call -> we can use MessagePostProcessor for several different calls

    // we replace this code with a method reference instead of a lambda (to avoid code duplication)
/*    @Override
    public void sendOrder(Order order) {
        jms.convertAndSend("tacocloud.order.queue", order, this::addOrderSource);
        System.out.println("Convert and sent order");
    }*/

    @Override
    public void sendOrder(Order order) {
        jms.convertAndSend(orderQueue, order, this::addOrderSource);
        System.out.println("Convert and sent order");
    }

    private Message addOrderSource(Message message) throws JMSException {
        message.setStringProperty("X_ORDER_SOURCE", "WEB");
        return message;
    }

    // for methods that use default-destination - make sure you have defined this property within application.yml from tacos module (that's where SpringApplication starts)

}
