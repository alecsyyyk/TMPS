package coffeeshop;

import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.domain.Espresso;
import coffeeshop.domain.Latte;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StateTest {

    // Helper: a ticket with one Latte, still in the New state.
    private OrderTicket ticketWithLatte() {
        OrderTicket ticket = new OrderTicket();
        ticket.addItem(new Latte());
        return ticket;
    }

    @Test
    void newTicketStartsInNewState() {
        OrderTicket ticket = new OrderTicket();

        assertEquals("New", ticket.getState().getName());
        assertFalse(ticket.getState().isFinal());
    }

    @Test
    void validTransitionsGoFromNewToCompleted() {
        OrderTicket ticket = ticketWithLatte();

        ticket.pay(new RecordingPayment());
        assertEquals("Paid", ticket.getState().getName());
        ticket.startPreparing();
        assertEquals("Preparing", ticket.getState().getName());
        ticket.markReady();
        assertEquals("Ready", ticket.getState().getName());
        ticket.pickUp();
        assertEquals("Completed", ticket.getState().getName());
        assertTrue(ticket.getState().isFinal());
    }

    @Test
    void forbiddenTransitionsAreRejectedAndStateDoesNotChange() {
        OrderTicket ticket = ticketWithLatte();

        assertThrows(IllegalStateException.class, ticket::pickUp);        // New -> Completed is not allowed
        assertThrows(IllegalStateException.class, ticket::startPreparing); // must pay first
        assertEquals("New", ticket.getState().getName());

        ticket.pay(new RecordingPayment());
        assertThrows(IllegalStateException.class, () -> ticket.addItem(new Espresso()));
        assertEquals(1, ticket.getOrder().getItems().size());

        ticket.startPreparing();
        assertThrows(IllegalStateException.class, ticket::cancel);         // too late to cancel
        assertEquals("Preparing", ticket.getState().getName());
    }

    @Test
    void finalStatesAllowNothing() {
        OrderTicket cancelled = ticketWithLatte();
        cancelled.cancel();

        assertEquals("Cancelled", cancelled.getState().getName());
        assertTrue(cancelled.getState().isFinal());
        assertThrows(IllegalStateException.class, () -> cancelled.pay(new RecordingPayment()));
        assertThrows(IllegalStateException.class, () -> cancelled.addItem(new Espresso()));
    }

    @Test
    void emptyOrderCannotBePaid() {
        OrderTicket ticket = new OrderTicket();
        RecordingPayment payment = new RecordingPayment();

        assertThrows(IllegalStateException.class, () -> ticket.pay(payment));
        assertTrue(payment.amounts.isEmpty());
        assertEquals("New", ticket.getState().getName());
    }
}
