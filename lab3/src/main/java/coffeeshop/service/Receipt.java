package coffeeshop.service;

import coffeeshop.domain.Order;

// DIP: abstraction for printing receipts.
public interface Receipt {
    void print(Order order);
}
