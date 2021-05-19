package tacos.kitchen.messaging.jms;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import tacos.Order;
import tacos.kitchen.OrderReceiver;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

// Receiving messages with JmsTemplate (pull model)
@Profile("jms-template")
@Component("templateOrderReceiver")
public class JmsOrderReceiver implements OrderReceiver {

    private JmsTemplate jms;
    private Destination orderQueue;

    private MessageConverter converter;
    // needed when we work with raw messages

    /*@Autowired
    public JmsOrderReceiver(JmsTemplate jms) {
        this.jms = jms;
    }*/

    /*@Autowired
    public JmsOrderReceiver(JmsTemplate jms, Destination orderQueue) {
        this.jms = jms;
        this.orderQueue = orderQueue;
    }*/

    /*@Autowired
    public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter) {
        this.jms = jms;
        this.converter = converter;
    }*/

    @Autowired
    public JmsOrderReceiver(JmsTemplate jms, MessageConverter converter, Destination orderQueue) {
        this.jms = jms;
        this.converter = converter;
        this.orderQueue = orderQueue;
    }
    // used when we work with raw messages

    // please uncomment what method you want to use

    // ----------------------------------- raw messages -----------------------------------
    // make sure to use a sendOrder() method that also works with raw messages & that the domain classes that you work with are serializable
    // (on send & receive side) & that you have defined a bean for MessageConverter in WebConfig.java
    // domain classes are - Ingredient.java, Taco.java, Order.java

    //  ======================== 1. method receive() ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) jms.receive();
    }*/
    // this method does not specify the location from where it receives the message - so we will use the destination from application.yml

    /*@Override
    public Order receiveOrder() {
        Message message = jms.receive();
        try {
            return (Order) converter.fromMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }*/
    // receive() returns an unconverted Message - so we injected a message converter to convert the message
    // The type ID property in the message will guide the converter in converting it to an Order, but it’s returned as an Object that requires casting before you can return it

    //  ======================== 2. method receive(Destination) ========================
    /*@Override
    public Order receiveOrder() {
        Message message = jms.receive(orderQueue);
        try {
            return (Order) converter.fromMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }*/

    //  ======================== 3. method receive(String) ========================
    /*@Override
    public Order receiveOrder() {
        Message message = jms.receive("tacocloud.order.queue");
        try {
            return (Order) converter.fromMessage(message);
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }*/


    // ----------------------------------- messages converted to domain types -----------------------------------

    //  ======================== 4. method receiveAndConvert() ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) jms.receiveAndConvert();
    }*/
    // we no longer need to inject a MessageConverter -> all of the message conversion will be done behind the scenes in receiveAndConvert()

    //  ======================== 5. method receiveAndConvert(Destination) ========================
    /*@Override
    public Order receiveOrder() {
        return (Order) jms.receiveAndConvert(orderQueue);
    }*/

    //  ======================== 6. method receiveAndConvert(String) ========================
    @Override
    public Order receiveOrder() {
        return (Order) jms.receiveAndConvert("tacocloud.order.queue");
    }

    // for methods that use default-destination - make sure you have defined this property within application.yml from tacos module (that's where SpringApplication starts)
}
