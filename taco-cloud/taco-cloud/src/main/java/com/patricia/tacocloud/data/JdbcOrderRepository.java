package com.patricia.tacocloud.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patricia.tacocloud.Order;
import com.patricia.tacocloud.Taco;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// saving data using SimpleJdbcInsert wrapper class
// SimpleJdbcInsert = is an object that wraps JdbcTemplate to make it easier to insert data into a table
@Repository
public class JdbcOrderRepository implements OrderRepository {

    // in the constructor we will create a couple of instances of SimpleJdbcInsert for inserting values into the Taco_Order and Taco_Order_Tacos tables
    private SimpleJdbcInsert orderInserter;
    private SimpleJdbcInsert orderTacoInserter;
    private ObjectMapper objectMapper;

    @Autowired
    public JdbcOrderRepository(JdbcTemplate jdbc) {
        this.orderInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order")
                .usingGeneratedKeyColumns("id");
        this.orderTacoInserter = new SimpleJdbcInsert(jdbc)
                .withTableName("Taco_Order_Tacos");
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Order save(Order order) {
        order.setPlacedAt(new Date());
        long orderId = saveOrderDetails(order);
        order.setId(orderId);
        List<Taco> tacos = order.getTacos();
        for (Taco taco : tacos) {
            saveTacoToOrder(taco, orderId);
        }
        return order;
    }
    // this method doesn't save anything -> it defines the flow for saving an Order and its associated Taco objects

    private void saveTacoToOrder(Taco taco, long orderId) {
        Map<String, Object> values = new HashMap<>();
        values.put("tacoOrder", orderId);
        values.put("taco", taco.getId());
        orderTacoInserter.execute(values);
    }
    // method execute() accepts a Map<Spring, Object> = the map keys correspond to the column names in the table the data is inserted into, the map values are inserted into those columns
    // we create the Map and set the appropriate values
    // a call to the orderTacoInserter’s execute() method performs the insert


    private long saveOrderDetails(Order order) {
        @SuppressWarnings("unchecked")
        Map<String, Object> values = objectMapper.convertValue(order, Map.class);
        values.put("placedAt", order.getPlacedAt());

        long orderId = orderInserter.executeAndReturnKey(values).longValue();
        return orderId;
    }
    // method executeAndReturnKey() accepts a Map<Spring, Object> = the map keys correspond to the column names in the table the data is inserted into, the map values are inserted into those columns
    // we copy the values from Order into entries of the Map
    // we use Jackson’s ObjectMapper and its convertValue() method to convert an Order into a Map
    // once the Map is created, you’ll set the placedAt entry to the value of the Order object’s placedAt property
    //      - it's necessary because ObjectMapper would otherwise convert the Date property to a long (which is incompatible with the placedAt field in the Taco_Order table)
    // now we have a map full of order data -> we call executeAndReturnKey() on orderInserter = this saves the order information to the Taco_Order table and returns the database-generated ID as a Number object
    // on the database-generated ID as a Number object is called longValue() which converts to a long value
}
// ■■■ Annotations:
// @Repository = you declare that - it should be automatically discovered by Spring component scanning and instantiated as a bean in the Spring application context
// @Autowired = when Spring creates the JdbcOrderRepository bean -> it injects it with JdbcTemplate through its constructor
//              - but instead of assigning JdbcTemplate directly to an instance variable - the constructor uses it to construct a couple of SimpleJdbcInsert instances
//                  - the first instance - which is assigned to the orderInserter instance variable works with the Taco_Order table
//                  - the second instance - which is assigned to the orderTacoInserter instance variable works with the Taco_Order_Tacos table
//                  - the last instance - is an instance of Jackson's ObjectMapper - usually is used for JSON processing - we will use it for save orders and their associated tacos

