package coffeeshop.behavioral.observer;

import coffeeshop.behavioral.state.OrderState;
import coffeeshop.behavioral.state.OrderTicket;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Observer - ConcreteObserver.
// Remembers every status change in a list. It does not print anything,
// it is used to show the history at the end and in the unit tests.
public class StatusHistoryLog implements OrderObserver {
    private final List<String> entries = new ArrayList<>();

    // Saves the change as a text like "#1 New -> Paid".
    @Override
    public void onStateChanged(OrderTicket ticket, OrderState oldState, OrderState newState) {
        entries.add("#" + ticket.getId() + " " + oldState.getName() + " -> " + newState.getName());
    }

    // Returns all saved changes (read-only).
    public List<String> getEntries() {
        return Collections.unmodifiableList(entries);
    }
}
