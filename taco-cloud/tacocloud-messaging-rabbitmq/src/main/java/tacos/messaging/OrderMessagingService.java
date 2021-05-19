package tacos.messaging;

import tacos.Order;

public interface OrderMessagingService {

    public void sendOrder(Order order);
}
