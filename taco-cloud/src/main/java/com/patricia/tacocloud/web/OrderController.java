package com.patricia.tacocloud.web;

import com.patricia.tacocloud.Order;
import com.patricia.tacocloud.User;
import com.patricia.tacocloud.data.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;

import javax.validation.Valid;
import java.security.Principal;

// After the user creates his taco, is redirected to an order form where he can place the order to have the taco delivered

@Slf4j
@Controller
@RequestMapping("/orders")
@SessionAttributes("order")
/*@ConfigurationProperties(prefix = "taco.orders")*/    // not needed because we use OrderProps
public class OrderController {

    // by default pageSize is 20, but it's changed using the value from application.yml for property taco.orders.pageSize
    /*private int pageSize = 20;

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }*/
    // it's not mandatory that the configuration properties to be set on a controller => we use holder class OrderProps
    // is replaced by:
    private OrderProps orderProps;

    private OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository, OrderProps orderProps) {
        this.orderRepository = orderRepository;
        this.orderProps = orderProps;
    }
    // here we inject OrderRepository & holder class OrderProps into OrderController

    @GetMapping("/current")                         // path here is: /orders/current
    public String orderForm(@AuthenticationPrincipal User user, @ModelAttribute Order order) {

        if (order.getDeliveryName() == null) {
            order.setDeliveryName(user.getFullname());
        }
        if (order.getDeliveryStreet() == null) {
            order.setDeliveryStreet(user.getStreet());
        }
        if (order.getDeliveryCity() == null) {
            order.setDeliveryCity(user.getCity());
        }
        if (order.getDeliveryState() == null) {
            order.setDeliveryState(user.getState());
        }
        if (order.getDeliveryZip() == null) {
            order.setDeliveryZip(user.getZip());
        } // all this auto-completes the above fields with the values from current user that is logged in

        return "orderForm";
    }

    @PostMapping                                    // path here is: /orders
    public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, @AuthenticationPrincipal User user) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        // - if there are any validation errors => those details are captures within an Errors object
        //      - if there are any Errors -> the method doesn't process the Order and returns "orderForm" which mean the form is redisplayed

        order.setUser(user);

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

    // the next method shows how @ConfigurationProperties works:
    @GetMapping
    public String ordersForUser(@AuthenticationPrincipal User user, Model model) {

        // we want to limit the numbers of orders displayed to the most recent 20 orders
        //Pageable pageable = PageRequest.of(0, pageSize);

        // we use the property pageSize from OrderProps
        Pageable pageable = PageRequest.of(0, orderProps.getPageSize());
        model.addAttribute("orders", orderRepository.findByUserOrderByPlacedAtDesc(user, pageable));
        return "orderList";
    }
}

// ■ Performing validations at form binding:
// @Valid = tells Spring MCV to perform validation on the submitted Order object after it's bound to the submitted form data  & before the processDesign() method is called

// Chapter 4:
// ways to determine who the user is for an Order:

// 1 - inject a Principal object into the controller method processOrder():
/*
public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, Principal principal) {
    ...
    User user = userRepository.findBuUsername(principal.getName());
    order.setUser(user);
    ...
}*/
// for this we will also need to create an UserRepository
// the problem with this is that we mix code that is related with security with non-related security code

// 2 - inject an Authentication object into the controller method processOrder():
/*
public String processOrder(@Valid Order order, Errors errors, SessionStatus sessionStatus, Authentication authentication) {
    ...
    User user = (User) authentication.getPrincipal();
    order.setUser(user);
    ...
}*/
// the problem with this is that the call getPrincipal() on the Authentication object - returns a java.util.Object => needs to be cast to User

// 3 - use an @AuthenticationPrincipal annotated method
// the cleanest solution of all

// 4 - use SecurityContextHolder to get at the security context
// the problem with this is that it's a bit messy & very heavy with security-specific code
// the advantage is that: it can be used anywhere in the application - not only in a controller's handler methods
/*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
User user = (User) authentication.getPrincipal();*/

// Chapter 5:
// @ConfigurationProperties(prefix = "taco.orders") = it specifies that the properties of that bean can be injected from properties in the Spring environment
// file additional-spring-configuration-metadata.json - is needed for declaring configuration property metadata (not so useful in IntelliJ)
