package tacos.web.api;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.Order;
import tacos.data.OrderRepository;

@RestController
@RequestMapping(path="/orders", produces="application/json")
@CrossOrigin(origins = "*")
public class OrderAPIController {

    private OrderRepository orderRepository;

    public OrderAPIController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }
    // here we inject OrderRepository into OrderController

    @GetMapping(produces = "application/json")
    public Iterable<Order> allOrders() {
        return orderRepository.findAll();
    }

    @PostMapping(consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public Order postOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    // ■ The endpoint that handles PUT requests for /orders/{orderId}  => replaces an Order resource
    @PutMapping(path = "/{orderId}", consumes = "application/json")
    public Order putOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    // ■ The endpoint that handles PATCH requests for /orders/{orderId}  => does a partial update of an Order resource
    @PatchMapping(path = "/{orderId}", consumes = "application/json")
    public Order patchOrder(@PathVariable("orderId") Long orderId, @RequestBody Order patchOrder) {

        Order order = orderRepository.findById(orderId).get();
        if (patchOrder.getDeliveryName() != null) {
            order.setDeliveryName(patchOrder.getDeliveryName());
        }
        if (patchOrder.getDeliveryStreet() != null) {
            order.setDeliveryStreet(patchOrder.getDeliveryStreet());
        }
        if (patchOrder.getDeliveryCity() != null) {
            order.setDeliveryCity(patchOrder.getDeliveryCity());
        }
        if (patchOrder.getDeliveryState() != null) {
            order.setDeliveryState(patchOrder.getDeliveryState());
        }
        if (patchOrder.getDeliveryZip() != null) {
            order.setDeliveryZip(patchOrder.getDeliveryState());
        }
        if (patchOrder.getCcNumber() != null) {
            order.setCcNumber(patchOrder.getCcNumber());
        }
        if (patchOrder.getCcExpiration() != null) {
            order.setCcExpiration(patchOrder.getCcExpiration());
        }
        if (patchOrder.getCcCVV() != null) {
            order.setCcCVV(patchOrder.getCcCVV());
        }
        return orderRepository.save(order);
    }
    // in this method we inspect each field of the incoming Order object & apply non-null values to the existing order
    // - allows the client to send only the properties that should be changed
    // - enables the server to retain existing data for any properties not specified by the client

    // ■ The endpoint that handles DELETE requests for /orders/{orderId}  => removes a resource
    @DeleteMapping("/{orderId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId) {
        try {
            orderRepository.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {}
    }
    // this method takes the Order by id and if that order exists -> the order will be deleted
    //                                       if that order does not exist -> EmptyResultDataAccessException will be thrown
}

// ■■■ Annotations:

// @RestController  - marks the class for discovery component scanning (like @Controller or @Service)
//                  = tells Spring that all handler methods in the controller should have their return value written directly to the body of the response

// @CrossOrigin = allows clients form any domain to consume the API
//      Angular part of the application - will be running on a separate host/port from the API
//      => The web browser will prevent the Angular client from consuming the API
//      Solution: this restriction can be solved by including CORS(Cross-Origin Resource Sharing) headers in the server application\
//                  - you can apply CORS in Spring with @CrossOrigin annotation

// @PutMapping  = specify that this method is responsible for PUT requests = does a whole replacement of the resource data
//              - PUT means = "put this data at this URL" - replacing any data that is already there
//                          - if any of the order's properties are omitted -> that property value will be overwritten with null

// @PatchMapping    = handles a PATCH request
//                  - even though PATCH implies a partial update -> it's up to you to write code in the handler method that performs the update

// @DeleteMapping   = handles a DELETE request - removes a resource

// @ResponseStatus(code = HttpStatus.NO_CONTENT) - so that the response will have the HTTP status of 204 - NO CONTENT = tells the client not to expect any content
