package coffeeshop.behavioral.command;

import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.domain.Product;

// Command - ConcreteCommand. The opposite of AddItemCommand.
public class RemoveItemCommand implements Command {
    private final OrderTicket ticket;
    private final Product product;

    // Saves the ticket and the product for later.
    public RemoveItemCommand(OrderTicket ticket, Product product) {
        this.ticket = ticket;
        this.product = product;
    }

    // Removes the product from the order.
    @Override
    public void execute() {
        ticket.removeItem(product);
    }

    // Puts the product back in the order.
    @Override
    public void undo() {
        ticket.addItem(product);
    }

    @Override
    public String getDescription() {
        return "Remove " + product.getName();
    }
}
