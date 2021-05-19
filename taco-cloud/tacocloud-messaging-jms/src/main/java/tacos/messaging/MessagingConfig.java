package tacos.messaging;

import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import tacos.Order;

import javax.jms.Destination;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

    @Bean
    public MappingJackson2MessageConverter messageConverter() {
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");

        Map<String, Class<?>> typeIdMappings = new HashMap<String, Class<?>>();
        typeIdMappings.put("order", Order.class);
        messageConverter.setTypeIdMappings(typeIdMappings);

        return messageConverter;
    }
    // we have this bean to use MappingJackson2MessageConverter instead od the default SimpleMessageConverter

    // setTypeIdPropertyName()  - it enables the receiver to know what type to convert an incoming message to
    // - by default it will contain the fully qualified classname of the type being converted - but that's inflexible requiring that
    // the receiver also have the same type with the same fully qualified classname

    // - we can map a synthetic type name to the actual type by calling setTypeIdMappings() on the message converter: here the bean method maps a synthetic order type ID to the Order class
    // - instead of the fully qualified classname being sent in the message’s _typeId property, the value order will be sent
    // - at the receiving application, a similar message converter will have been configured, mapping order to its own understanding of what an order is

    // if you are working with Destination obj -> you have to declare a Destination bean first & then inject it into the bean that performs messaging
    // make sure that you declare the bean in a @Configuration class
    @Bean
    public Destination orderQueue() {
        return new ActiveMQQueue("tacocloud.order.queue");
    }
}
