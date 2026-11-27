package coffeeshop;

import coffeeshop.behavioral.observer.StatusHistoryLog;
import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.domain.Latte;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ObserverTest {

    // Helper: a ticket with one product, so it can be paid.
    private OrderTicket ticketWithLatte() {
        OrderTicket ticket = new OrderTicket();
        ticket.addItem(new Latte());
        return ticket;
    }

    @Test
    void allSubscribedObserversAreNotified() {
        OrderTicket ticket = ticketWithLatte();
        StatusHistoryLog first = new StatusHistoryLog();
        StatusHistoryLog second = new StatusHistoryLog();
        ticket.subscribe(first);
        ticket.subscribe(second);

        ticket.pay(new RecordingPayment());
        ticket.startPreparing();

        String id = "#" + ticket.getId();
        List<String> expected = List.of(id + " New -> Paid", id + " Paid -> Preparing");
        assertEquals(expected, first.getEntries());
        assertEquals(expected, second.getEntries());
    }

    @Test
    void unsubscribedObserverIsNotNotifiedAnymore() {
        OrderTicket ticket = ticketWithLatte();
        StatusHistoryLog stays = new StatusHistoryLog();
        StatusHistoryLog leaves = new StatusHistoryLog();
        ticket.subscribe(stays);
        ticket.subscribe(leaves);

        ticket.pay(new RecordingPayment());
        ticket.unsubscribe(leaves);
        ticket.startPreparing();
        ticket.markReady();

        assertEquals(3, stays.getEntries().size());
        assertEquals(1, leaves.getEntries().size());
        assertFalse(ticket.isSubscribed(leaves));
    }

    @Test
    void sameObserverSubscribedTwiceIsNotifiedOnce() {
        OrderTicket ticket = ticketWithLatte();
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(log);
        ticket.subscribe(log);

        ticket.cancel();

        assertEquals(1, log.getEntries().size());
    }

    @Test
    void rejectedActionDoesNotNotifyObservers() {
        OrderTicket ticket = ticketWithLatte();
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(log);

        try {
            ticket.pickUp(); // not allowed in New
        } catch (IllegalStateException expected) {
            // ok
        }

        assertTrue(log.getEntries().isEmpty());
    }
}
