package tacos;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

// Besides the basic CRUD operations provided by CrudRepository - we can define ours by adding the method declaration to the Repository => Spring Data will generate the implementation
// Repository methods are composed of:
//          verb + optional subject + "By" + a predicate

public interface CustomizedJPARepository extends CrudRepository<Order, Long> {

    // examples:

    // ex1: fetch all the orders delivered to a given zip code
    List<Order> findByDeliveryZip(String deliveryZip);
    // Spring Data knows this method tries to find Orders = find all Orders entities by matching their deliveryZip property with the value passed as a parameter to the method
    // verb = find, predicate = deliveryZip, subject - implied is Order

    // ex2: query for all orders delivered to a given zip code within a given date range
    List<Order> readOrdersByDeliveryZipAndPlacedAtBetween(String deliveryZip, Date startDate, Date endDate);
    // verb = find, subject = orders, predicate = deliveryZip & placedAt , between - means the value must fall between the given times

    // ex3
    List<Order> findByDeliveryStreetAndDeliveryCityAllIgnoreCase(String deliveryTo, String deliveryCity);

    // ex4
    List<Order> findByDeliveryCityOrderByDeliveryStreet(String city);

    // ex5: for complex queries which is impossible to achieve using naming conventions
    @Query("from Order o where o.deliveryCity='Seattle'")
    List<Order> readOrdersDeliveredInSeattle();
    // if you run the above query make sure all the tables are mapped otherwise you get: Taco_Order is not mapped
}


