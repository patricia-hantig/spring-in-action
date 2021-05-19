package tacos.kitchen.messaging.kafka.listener;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import tacos.Order;
import tacos.kitchen.KitchenUI;

// Receiving messages with KafkaListener
@Profile("kafka-listener")
@Component
@Slf4j
public class OrderListener {

    private KitchenUI ui;

    @Autowired
    public OrderListener(KitchenUI ui) {
        this.ui = ui;
    }

    // please uncomment what method you want to use:

    // 1. method that uses the payload as a parameter(Order order)
    @KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(Order order) {
        ui.displayOrder(order);
        System.out.println("The order was received");
    }

    // if we want additional metadata from the message besides the payload we can use a ConsumerRecord object or a Message object

    // 2. method that uses the payload as a parameter & a ConsumerRecord object
    /*@KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(Order order, ConsumerRecord<String, Order> record) {
        log.info("Received from partition {} with timestamp {}", record.partition(), record.timestamp());
        ui.displayOrder(order);
    }*/

    // 3. method that uses the payload as a parameter & a Message object
    /*@KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(Order order, Message<Order> message) {
        MessageHeaders headers = message.getHeaders();
        log.info("Received from partition {} with timestamp {}",
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_TIMESTAMP));
        ui.displayOrder(order);
    }*/

    // message payload is also available via: ConsumerRecord.value()

    // 4. method that uses a ConsumerRecord object
    /*@KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(ConsumerRecord<String, Order> record) {
        log.info("Received from partition {} with timestamp {}", record.partition(), record.timestamp());
        ui.displayOrder(record.value());
    }*/

    // message payload is also available via: Message.getPayload()

    // 5. method that uses a Message object
    /*@KafkaListener(topics = "tacocloud.orders.topic")
    public void handle(Order order, Message<Order> message) {
        MessageHeaders headers = message.getHeaders();
        log.info("Received from partition {} with timestamp {}",
                headers.get(KafkaHeaders.RECEIVED_PARTITION_ID),
                headers.get(KafkaHeaders.RECEIVED_TIMESTAMP));
        ui.displayOrder(message.getPayload());
    }*/
}
