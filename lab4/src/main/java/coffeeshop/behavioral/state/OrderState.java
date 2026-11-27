package coffeeshop.behavioral.state;

import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;

// State - State interface.
// Every status of an order (New, Paid, Preparing, ...) is a class that
// implements this interface. The OrderTicket just calls the method on its
// current state, and the state decides if the action is allowed and which
// state comes next.
public interface OrderState {

    // The name of the state, for example "New" or "Paid".
    String getName();

    // A short text for the customer, for example "We are making your order".
    String getMessage();

    // True if the order is finished (completed or cancelled) and nothing
    // more can happen to it.
    boolean isFinal();

    // Adds a product to the order.
    void addItem(OrderTicket ticket, Product product);

    // Removes a product from the order.
    void removeItem(OrderTicket ticket, Product product);

    // Pays the order with the given payment method.
    void pay(OrderTicket ticket, Payment payment);

    // Gives the money back (undo of a payment).
    void refund(OrderTicket ticket);

    // The barista starts making the order.
    void startPreparing(OrderTicket ticket);

    // The order is ready on the counter.
    void markReady(OrderTicket ticket);

    // The customer takes the order.
    void pickUp(OrderTicket ticket);

    // The order is cancelled.
    void cancel(OrderTicket ticket);
}
