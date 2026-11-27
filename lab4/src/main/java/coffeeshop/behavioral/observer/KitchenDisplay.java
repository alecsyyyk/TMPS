package coffeeshop.behavioral.observer;

import coffeeshop.behavioral.state.OrderState;
import coffeeshop.behavioral.state.OrderTicket;

// Observer - ConcreteObserver.
// The screen in the kitchen: it shows every status change so the barista
// knows what to do.
public class KitchenDisplay implements OrderObserver {

    // Prints one line like: [Kitchen] Order #1: New -> Paid
    @Override
    public void onStateChanged(OrderTicket ticket, OrderState oldState, OrderState newState) {
        System.out.println("[Kitchen] Order #" + ticket.getId() + ": "
                + oldState.getName() + " -> " + newState.getName());
    }
}
