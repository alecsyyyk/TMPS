package coffeeshop.behavioral.state;

// State - ConcreteState "Completed".
// Final state: the customer has the order. No action is allowed anymore,
// so this class does not override any action from BaseOrderState.
public class CompletedState extends BaseOrderState {

    @Override
    public String getName() {
        return "Completed";
    }

    @Override
    public String getMessage() {
        return "Enjoy your coffee! Thank you.";
    }

    // This is an end state.
    @Override
    public boolean isFinal() {
        return true;
    }
}
