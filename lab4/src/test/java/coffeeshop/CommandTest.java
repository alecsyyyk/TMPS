package coffeeshop;

import coffeeshop.behavioral.command.AddItemCommand;
import coffeeshop.behavioral.command.CommandHistory;
import coffeeshop.behavioral.command.PayOrderCommand;
import coffeeshop.behavioral.command.RemoveItemCommand;
import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.domain.Croissant;
import coffeeshop.domain.Latte;
import coffeeshop.domain.Product;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CommandTest {
    private static final double DELTA = 1e-9;

    @Test
    void executeThenUndoRestoresTheOriginalOrder() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();
        Product latte = new Latte();

        history.run(new AddItemCommand(ticket, latte));
        assertEquals(40.0, ticket.getOrder().getTotal(), DELTA);

        history.undoLast();
        assertEquals(0.0, ticket.getOrder().getTotal(), DELTA);
        assertEquals(0, history.size());
    }

    @Test
    void commandsAreUndoneInReverseOrder() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();
        Product latte = new Latte();
        Product croissant = new Croissant();

        history.run(new AddItemCommand(ticket, latte));
        history.run(new AddItemCommand(ticket, croissant));
        history.run(new RemoveItemCommand(ticket, latte));
        assertEquals(List.of(croissant), ticket.getOrder().getItems());

        history.undoLast(); // latte is back
        assertEquals(2, ticket.getOrder().getItems().size());
        history.undoLast(); // croissant removed
        assertEquals(List.of(latte), ticket.getOrder().getItems());
    }

    @Test
    void undoOfPaymentIsARefundAndGoesBackToNew() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();
        RecordingPayment payment = new RecordingPayment();

        history.run(new AddItemCommand(ticket, new Latte()));
        history.run(new PayOrderCommand(ticket, payment));
        assertEquals("Paid", ticket.getState().getName());
        assertEquals(List.of(40.0), payment.amounts);

        history.undoLast();
        assertEquals("New", ticket.getState().getName());
    }

    @Test
    void failedCommandIsNotSavedInHistory() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();

        // Paying an empty order is rejected by the state.
        assertThrows(IllegalStateException.class,
                () -> history.run(new PayOrderCommand(ticket, new RecordingPayment())));
        assertEquals(0, history.size());
        assertFalse(history.undoLast()); // nothing to undo, no crash
    }

    @Test
    void undoThatTheStateForbidsKeepsTheCommandInHistory() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();
        history.run(new AddItemCommand(ticket, new Latte()));
        history.run(new PayOrderCommand(ticket, new RecordingPayment()));
        ticket.startPreparing();

        // Too late for a refund: the barista already started.
        assertThrows(IllegalStateException.class, history::undoLast);
        assertEquals(2, history.size());
        assertEquals("Preparing", ticket.getState().getName());
    }
}
