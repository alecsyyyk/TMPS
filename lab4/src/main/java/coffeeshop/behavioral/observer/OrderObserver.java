package coffeeshop.behavioral.observer;

import coffeeshop.behavioral.state.OrderState;
import coffeeshop.behavioral.state.OrderTicket;

// Observer - Observer interface.
// Anything that wants to know when an order changes its state implements
// this interface and subscribes to an OrderTicket.
// The ticket only knows this interface, never the concrete observers.
public interface OrderObserver {

    // Called by the ticket every time its state changes.
    void onStateChanged(OrderTicket ticket, OrderState oldState, OrderState newState);
}
