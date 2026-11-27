package coffeeshop.behavioral.state;

import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;

// State - base class for the concrete states.
// By default every action is forbidden and throws an exception.
// A concrete state only overrides the actions that are allowed in it,
// so each state class stays short and easy to read.
public abstract class BaseOrderState implements OrderState {

    // Most states are not final, so the default answer is false.
    @Override
    public boolean isFinal() {
        return false;
    }

    @Override
    public void addItem(OrderTicket ticket, Product product) {
        throw notAllowed("add a product");
    }

    @Override
    public void removeItem(OrderTicket ticket, Product product) {
        throw notAllowed("remove a product");
    }

    @Override
    public void pay(OrderTicket ticket, Payment payment) {
        throw notAllowed("pay");
    }

    @Override
    public void refund(OrderTicket ticket) {
        throw notAllowed("refund");
    }

    @Override
    public void startPreparing(OrderTicket ticket) {
        throw notAllowed("start preparing");
    }

    @Override
    public void markReady(OrderTicket ticket) {
        throw notAllowed("mark as ready");
    }

    @Override
    public void pickUp(OrderTicket ticket) {
        throw notAllowed("pick up");
    }

    @Override
    public void cancel(OrderTicket ticket) {
        throw notAllowed("cancel");
    }

    // Builds the error that is thrown when an action is not allowed.
    protected IllegalStateException notAllowed(String action) {
        return new IllegalStateException("Cannot " + action + ": the order is " + getName() + ".");
    }
}
