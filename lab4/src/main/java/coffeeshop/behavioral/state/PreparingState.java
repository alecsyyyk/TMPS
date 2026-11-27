package coffeeshop.behavioral.state;

// State - ConcreteState "Preparing".
// The barista is making the coffee. It cannot be cancelled anymore.
// The only next step is: mark ready (-> Ready).
public class PreparingState extends BaseOrderState {

    @Override
    public String getName() {
        return "Preparing";
    }

    @Override
    public String getMessage() {
        return "The barista is making your order.";
    }

    // The coffee is done and is put on the counter.
    @Override
    public void markReady(OrderTicket ticket) {
        ticket.changeState(new ReadyState());
    }
}
