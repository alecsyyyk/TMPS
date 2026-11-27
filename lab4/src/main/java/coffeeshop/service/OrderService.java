package coffeeshop.service;

import coffeeshop.domain.Order;
import coffeeshop.payment.Payment;

// DIP: depends on abstractions injected through the constructor.
// SRP: coordinates the order workflow.
// It does not know that `payment` is a PaymentProxy or that the order holds
// decorators and bundles - it only sees Payment and Order.getTotal().
public class OrderService {
    private final Payment payment;
    private final OrderRepository repository;
    private final Receipt receipt;

    public OrderService(Payment payment, OrderRepository repository, Receipt receipt) {
        this.payment = payment;
        this.repository = repository;
        this.receipt = receipt;
    }

    // If the payment is declined, the exception stops the workflow and the
    // order is neither saved nor printed.
    public void placeOrder(Order order) {
        if (order.isEmpty()) {
            throw new IllegalArgumentException("An order cannot be placed without products.");
        }
        payment.pay(order.getTotal());
        repository.save(order);
        receipt.print(order);
    }
}
