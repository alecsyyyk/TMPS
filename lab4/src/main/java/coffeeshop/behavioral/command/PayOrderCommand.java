package coffeeshop.behavioral.command;

import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.payment.Payment;

// Command - ConcreteCommand.
// Pays the order. Undo means a refund: the order goes back to New.
// The ticket's state decides if paying / refunding is allowed right now.
public class PayOrderCommand implements Command {
    private final OrderTicket ticket;
    private final Payment payment;

    // Saves the ticket and the payment method for later.
    public PayOrderCommand(OrderTicket ticket, Payment payment) {
        this.ticket = ticket;
        this.payment = payment;
    }

    // Pays the whole order.
    @Override
    public void execute() {
        ticket.pay(payment);
    }

    // Gives the money back (only possible while the order is Paid).
    @Override
    public void undo() {
        ticket.refund();
    }

    @Override
    public String getDescription() {
        return "Pay order #" + ticket.getId();
    }
}
