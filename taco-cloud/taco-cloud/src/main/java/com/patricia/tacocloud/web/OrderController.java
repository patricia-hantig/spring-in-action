package com.patricia.tacocloud.web;

import com.patricia.tacocloud.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

// After the user creates his taco, is redirected to an order form where he can place the order to have the taco delivered

@Slf4j
@Controller
@RequestMapping("/orders")
public class OrderController {

    @GetMapping("/current")                         // path here is: /orders/current
    public String orderForm(Model model) {
        model.addAttribute("order", new Order());
        return "orderForm";
    }

    @PostMapping                                    // path here is: /orders
    public String processOrder(@Valid Order order, Errors errors) {
        if (errors.hasErrors()) {
            return "orderForm";
        }
        // - if there are any validation errors => those details are captures within an Errors object
        //      - if there are any Errors -> the method doesn't process the Order and returns "orderForm" which mean the form is redisplayed

        log.info("Order submitted: " + order);
        return "redirect:/";
    }
    // when this method is called - it's given an Order object whose properties are bound to the submitted form fields
    // returns "redirect:/" => after the method completes -> the user browser is redirected to the relative path: /
}

// ■ Performing validations at form binding:
// @Valid = tells Spring MCV to perform validation on the submitted Order object after it's bound to the submitted form data  & before the processDesign() method is called

