package coffeeshop.behavioral.command;

import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.domain.Product;

// Command - ConcreteCommand. The receiver is the OrderTicket.
// The command does not contain business logic: it only remembers WHAT to do
// (which product, which ticket) and calls the ticket.
public class AddItemCommand implements Command {
    private final OrderTicket ticket;
    private final Product product;

    // Saves the ticket and the product for later.
    public AddItemCommand(OrderTicket ticket, Product product) {
        this.ticket = ticket;
        this.product = product;
    }

    // Adds the product to the order.
    @Override
    public void execute() {
        ticket.addItem(product);
    }

    // Removes the same product again.
    @Override
    public void undo() {
        ticket.removeItem(product);
    }

    @Override
    public String getDescription() {
        return "Add " + product.getName();
    }
}
