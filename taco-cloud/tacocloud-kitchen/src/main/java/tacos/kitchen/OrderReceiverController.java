package tacos.kitchen;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.Order;

@Profile({"jms-template", "rabbitmq-template"})
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderReceiverController {

    private final OrderReceiver orderReceiver;

    @GetMapping(path = "/receive")
    public String receiveOrder(Model model) {
        Order order = orderReceiver.receiveOrder();
        if (order != null) {
            model.addAttribute("order", order);
            System.out.println("The order was received with delivery name = " + order.getDeliveryName());
            return "receiveOrder";
        }
        System.out.println("There is a problem with the order and was not received");
        return "noOrder";
    }

}
