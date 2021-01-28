package com.patricia.tacocloud.data;

import com.patricia.tacocloud.Order;

public interface OrderRepository {

    Order save(Order order);
}
