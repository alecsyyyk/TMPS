package coffeeshop;

import coffeeshop.behavioral.command.AddItemCommand;
import coffeeshop.behavioral.command.CommandHistory;
import coffeeshop.behavioral.command.PayOrderCommand;
import coffeeshop.behavioral.observer.StatusHistoryLog;
import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.domain.Latte;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.proxy.PaymentProxy;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// One execution path through several patterns:
// Command -> State (OrderTicket) -> Proxy (payment) -> state change -> Observer,
// with products made by Factory Method, Builder and Decorator.
class BehavioralInteractionTest {
    private static final double DELTA = 1e-9;

    @Test
    void payCommandChangesStateAndObserversAreNotified() {
        OrderTicket ticket = new OrderTicket();
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(log);
        CommandHistory history = new CommandHistory();
        RecordingPayment real = new RecordingPayment();

        history.run(new AddItemCommand(ticket,
                new CustomCoffeeBuilder(new Latte()).milk(MilkType.OAT).extraShot().build())); // 58
        history.run(new AddItemCommand(ticket, new OfficePackCreator().createProduct()));      // 142
        history.run(new PayOrderCommand(ticket, new PaymentProxy(() -> real, 1000.0)));

        assertEquals(List.of(200.0), real.amounts);
        assertEquals("Paid", ticket.getState().getName());
        assertEquals(List.of("#" + ticket.getId() + " New -> Paid"), log.getEntries());

        history.undoLast(); // refund
        assertEquals("New", ticket.getState().getName());
        assertEquals(2, log.getEntries().size());
    }

    @Test
    void declinedPaymentKeepsStateAndSendsNoNotification() {
        OrderTicket ticket = new OrderTicket();
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(log);
        CommandHistory history = new CommandHistory();
        RecordingPayment real = new RecordingPayment();

        history.run(new AddItemCommand(ticket, new OfficePackCreator().createProduct())); // 142
        assertThrows(PaymentDeclinedException.class,
                () -> history.run(new PayOrderCommand(ticket, new PaymentProxy(() -> real, 100.0))));

        assertEquals("New", ticket.getState().getName());
        assertTrue(real.amounts.isEmpty());
        assertTrue(log.getEntries().isEmpty());
        assertEquals(1, history.size()); // only the AddItemCommand
        assertEquals(142.0, ticket.getOrder().getTotal(), DELTA);
    }
}
