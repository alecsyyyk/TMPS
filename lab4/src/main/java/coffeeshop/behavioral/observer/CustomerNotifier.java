package coffeeshop.behavioral.observer;

import coffeeshop.behavioral.state.OrderState;
import coffeeshop.behavioral.state.OrderTicket;

// Observer - ConcreteObserver.
// Sends a friendly message to the customer (here we just print it,
// in a real app it could be an SMS or a push notification).
// The customer can turn it on and off from the console menu.
public class CustomerNotifier implements OrderObserver {
    private final String customerName;

    // Creates a notifier for one customer.
    public CustomerNotifier(String customerName) {
        this.customerName = customerName;
    }

    // Prints the message of the new state for the customer.
    @Override
    public void onStateChanged(OrderTicket ticket, OrderState oldState, OrderState newState) {
        System.out.println("[SMS to " + customerName + "] " + newState.getMessage());
    }
}
