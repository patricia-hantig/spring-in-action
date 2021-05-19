package tacos.messaging;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;
import tacos.Order;

import java.util.Date;

@Service
public class KafkaOrderMessagingService implements OrderMessagingService {

    private KafkaTemplate<String, Order> kafkaTemplate;

    @Autowired
    public KafkaOrderMessagingService(KafkaTemplate<String, Order> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    // please uncomment what method you want to use

    // ----------------------------------- send() method -----------------------------------

    //  ======================== 1. method send(topic, payload) ========================
    @Override
    public void sendOrder(Order order) {
        kafkaTemplate.send("tacocloud.orders.topic", order);
        System.out.println("The order was sent");
    }

    // alternative implementation using handler for success/error
    /*@Override
    public void sendOrder(Order order) {
        ListenableFuture<SendResult<String, Order>> tacocloudOrder = kafkaTemplate.send("tacocloud.orders.topic", order);
        tacocloudOrder.addCallback(new ListenableFutureCallback<SendResult<String, Order>>() {
            @Override
            public void onFailure(Throwable throwable) {
                System.out.println("Unable to send order=[" + tacocloudOrder + "] due to " + throwable.getMessage());
            }

            @Override
            public void onSuccess(SendResult<String, Order> result) {
                System.out.println("Sent order=[" + tacocloudOrder + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
        });
    }*/

    //  ======================== 2. method send(topic, key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.send("tacocloud.orders.topic", "order", order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 3. method send(topic, partition, key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.send("tacocloud.orders.topic", 0, "order", order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 4. method send(topic, partition, timestamp, key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        Long timestampPlus5Sec = new Date().getTime() + 5000;
        kafkaTemplate.send("tacocloud.orders.topic", 0, timestampPlus5Sec, "order", order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 5. method send(ProducerRecord) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.send(new ProducerRecord<>("tacocloud.orders.topic", order));
        System.out.println("The order was sent");
    }*/

    //  ======================== 6. method send(Message) ========================
    /*@Override
    public void sendOrder(Order order) {
        Message<Order> message = MessageBuilder.withPayload(order).build();
        kafkaTemplate.send(message);
        System.out.println("The order was sent");
    }*/


    // ----------------------------------- sendDefault() method -----------------------------------

    //  ======================== 7. method sendDefault(payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.sendDefault(order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 8. method sendDefault(key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.sendDefault("order", order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 9. method sendDefault(partition, key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        kafkaTemplate.sendDefault(0, "order", order);
        System.out.println("The order was sent");
    }*/

    //  ======================== 10. method sendDefault(partition, timestamp, key, payload) ========================
    /*@Override
    public void sendOrder(Order order) {
        Long timestampPlus5Sec = new Date().getTime() + 5000;
        kafkaTemplate.sendDefault(0, timestampPlus5Sec, "order", order);
        System.out.println("The order was sent");
    }*/
}
