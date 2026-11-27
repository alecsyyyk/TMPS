package coffeeshop.behavioral.state;

// State - ConcreteState "Paid".
// The money is taken, but the barista has not started yet.
// From here: start preparing (-> Preparing), refund (-> New) or cancel (-> Cancelled).
public class PaidState extends BaseOrderState {

    @Override
    public String getName() {
        return "Paid";
    }

    @Override
    public String getMessage() {
        return "Payment received, your order is in the queue.";
    }

    // The barista takes the order.
    @Override
    public void startPreparing(OrderTicket ticket) {
        ticket.changeState(new PreparingState());
    }

    // The money goes back and the order is open again (used by undo).
    @Override
    public void refund(OrderTicket ticket) {
        System.out.printf("Refunding %.2f MDL...%n", ticket.getOrder().getTotal());
        ticket.changeState(new NewState());
    }

    // The customer changed their mind: give the money back and cancel.
    @Override
    public void cancel(OrderTicket ticket) {
        System.out.printf("Refunding %.2f MDL...%n", ticket.getOrder().getTotal());
        ticket.changeState(new CancelledState());
    }
}
