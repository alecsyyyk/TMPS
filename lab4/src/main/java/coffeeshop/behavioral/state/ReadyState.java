package coffeeshop.behavioral.state;

// State - ConcreteState "Ready".
// The order waits on the counter. The only next step is: pick up (-> Completed).
public class ReadyState extends BaseOrderState {

    @Override
    public String getName() {
        return "Ready";
    }

    @Override
    public String getMessage() {
        return "Your order is ready, please take it from the counter!";
    }

    // The customer takes the order, so it is finished.
    @Override
    public void pickUp(OrderTicket ticket) {
        ticket.changeState(new CompletedState());
    }
}
