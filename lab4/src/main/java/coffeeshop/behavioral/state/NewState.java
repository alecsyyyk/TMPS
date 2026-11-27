package coffeeshop.behavioral.state;

import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;

// State - ConcreteState "New".
// The order is still being filled: products can be added and removed.
// From here the order can be paid (-> Paid) or cancelled (-> Cancelled).
public class NewState extends BaseOrderState {

    @Override
    public String getName() {
        return "New";
    }

    @Override
    public String getMessage() {
        return "Your order is open, you can still add products.";
    }

    // In the New state we are allowed to add products.
    @Override
    public void addItem(OrderTicket ticket, Product product) {
        ticket.getOrder().addItem(product);
    }

    // In the New state we are allowed to remove products.
    @Override
    public void removeItem(OrderTicket ticket, Product product) {
        boolean removed = ticket.getOrder().removeItem(product);
        if (!removed) {
            throw new IllegalArgumentException("This product is not in the order.");
        }
    }

    // Pays the order. If the payment is declined (for example by the
    // PaymentProxy from Lab 3) an exception is thrown and the state stays New.
    @Override
    public void pay(OrderTicket ticket, Payment payment) {
        if (ticket.getOrder().isEmpty()) {
            throw new IllegalStateException("Cannot pay: the order has no products.");
        }
        payment.pay(ticket.getOrder().getTotal());
        ticket.changeState(new PaidState());
    }

    // A new order can be cancelled without any refund.
    @Override
    public void cancel(OrderTicket ticket) {
        ticket.changeState(new CancelledState());
    }
}
