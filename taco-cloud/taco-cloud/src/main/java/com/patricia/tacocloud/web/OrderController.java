package com.patricia.tacocloud.web;

import com.patricia.tacocloud.Order;
import com.patricia.tacocloud.data.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;

// After the user creates his taco, is redirected to an order form where he can place the order to have the taco delivered

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
public class OrderController {

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    // here we inject OrderRepository into OrderController

    @GetMapping("/current")                         // path here is: /orders/current
    public String orderForm() {
        return "orderForm";
    }

    @PostMapping                                    // path here is: /orders
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        // - if there are any validation errors => those details are captures within an Errors object
        //      - if there are any Errors -> the method doesn't process the Order and returns "orderForm" which mean the form is redisplayed

        orderRepository.save(order);
        sessionStatus.setComplete();

        log.info("Order submitted: " + order);
        return "redirect:/";
    }
    // when this method is called - it's given an Order object whose properties are bound to the submitted form fields
    // returns "redirect:/" => after the method completes -> the user browser is redirected to the relative path: /

    // here the Order object submitted in the form (which also happens to be the same Order object maintained in session) is saved via the save() method on the injected OrderRepository
    // after the order is saved - we don't need it in the session anymore so
    //          the processOrder() method asks for a SessionStatus parameter and calls its setComplete() method to reset the session
}

// ■ Performing validations at form binding:
// @Valid = tells Spring MCV to perform validation on the submitted Order object after it's bound to the submitted form data  & before the processDesign() method is called

