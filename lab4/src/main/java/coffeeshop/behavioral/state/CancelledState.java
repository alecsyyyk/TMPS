package coffeeshop.behavioral.state;

// State - ConcreteState "Cancelled".
// Final state: the order was cancelled. No action is allowed anymore.
public class CancelledState extends BaseOrderState {

    @Override
    public String getName() {
        return "Cancelled";
    }

    @Override
    public String getMessage() {
        return "Your order was cancelled.";
    }

    // This is an end state.
    @Override
    public boolean isFinal() {
        return true;
    }
}
