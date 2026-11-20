package coffeeshop.service;

import coffeeshop.domain.Order;

// DIP: abstraction for saving orders.
public interface OrderRepository {
    void save(Order order);
}
