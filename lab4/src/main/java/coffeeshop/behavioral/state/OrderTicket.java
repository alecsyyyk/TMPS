package coffeeshop.behavioral.state;

import coffeeshop.behavioral.observer.OrderObserver;
import coffeeshop.domain.Order;
import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;

import java.util.ArrayList;
import java.util.List;

// State - Context and Observer - Subject.
//
// An OrderTicket is an Order (from Lab 1-3) plus its current status.
//  - State: the ticket does not use if/else or switch on the status. Every
//    action is passed to the current state object, which decides what to do.
//  - Observer: every time the state changes, all subscribed observers are
//    notified. Observers can subscribe and unsubscribe while the app runs.
public class OrderTicket {
    private static int nextId = 1;

    private final int id;
    private final Order order;
    private OrderState state;
    private final List<OrderObserver> observers = new ArrayList<>();

    // Creates a new ticket. Every ticket starts in the New state.
    public OrderTicket() {
        this.id = nextId;
        nextId++;
        this.order = new Order();
        this.state = new NewState();
    }

    // ----- getters -----

    public int getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public OrderState getState() {
        return state;
    }

    // ----- actions: each one is delegated to the current state -----

    public void addItem(Product product) {
        state.addItem(this, product);
    }

    public void removeItem(Product product) {
        state.removeItem(this, product);
    }

    public void pay(Payment payment) {
        state.pay(this, payment);
    }

    public void refund() {
        state.refund(this);
    }

    public void startPreparing() {
        state.startPreparing(this);
    }

    public void markReady() {
        state.markReady(this);
    }

    public void pickUp() {
        state.pickUp(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    // ----- Observer: subscribe / unsubscribe / notify -----

    // Adds an observer. The same observer is not added twice.
    public void subscribe(OrderObserver observer) {
        if (observer == null) {
            throw new IllegalArgumentException("Observer cannot be null.");
        }
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    // Removes an observer, it will not receive messages anymore.
    public void unsubscribe(OrderObserver observer) {
        observers.remove(observer);
    }

    // Checks if an observer is subscribed (used by the console menu).
    public boolean isSubscribed(OrderObserver observer) {
        return observers.contains(observer);
    }

    // Called only by the state classes (package-private) to move to the
    // next state. After the change, every observer is told about it.
    void changeState(OrderState newState) {
        OrderState oldState = this.state;
        this.state = newState;
        notifyObservers(oldState, newState);
    }

    // Sends the change to every observer. We loop over a copy of the list,
    // so an observer can unsubscribe itself without breaking the loop.
    private void notifyObservers(OrderState oldState, OrderState newState) {
        List<OrderObserver> copy = new ArrayList<>(observers);
        for (OrderObserver observer : copy) {
            observer.onStateChanged(this, oldState, newState);
        }
    }
}
