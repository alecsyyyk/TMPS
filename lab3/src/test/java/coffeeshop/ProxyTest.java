package coffeeshop;

import coffeeshop.creational.factory.CashPaymentCreator;
import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.structural.proxy.PaymentProxy;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProxyTest {
    @Test
    void authorizedPaymentPassesThroughUnchanged() {
        RecordingPayment real = new RecordingPayment();
        Payment proxy = new PaymentProxy(() -> real, 100.0);

        proxy.pay(42.5);
        proxy.pay(100.0); // exactly at the limit is allowed

        assertEquals(List.of(42.5, 100.0), real.amounts);
    }

    @Test
    void paymentAboveLimitIsBlockedAndRealPaymentIsNeverCreated() {
        AtomicInteger created = new AtomicInteger();
        Payment proxy = new PaymentProxy(() -> {
            created.incrementAndGet();
            return new RecordingPayment();
        }, 100.0);

        assertThrows(PaymentDeclinedException.class, () -> proxy.pay(100.01));
        assertEquals(0, created.get());
    }

    @Test
    void nonPositiveAmountIsBlocked() {
        RecordingPayment real = new RecordingPayment();
        Payment proxy = new PaymentProxy(() -> real, 100.0);

        assertThrows(PaymentDeclinedException.class, () -> proxy.pay(0));
        assertThrows(PaymentDeclinedException.class, () -> proxy.pay(-5));
        assertTrue(real.amounts.isEmpty());
    }

    @Test
    void realPaymentIsCreatedLazilyAndOnlyOnce() {
        AtomicInteger created = new AtomicInteger();
        RecordingPayment real = new RecordingPayment();
        Payment proxy = new PaymentProxy(() -> {
            created.incrementAndGet();
            return real;
        }, 100.0);

        assertEquals(0, created.get());
        proxy.pay(10);
        proxy.pay(20);

        assertEquals(1, created.get());
        assertEquals(List.of(10.0, 20.0), real.amounts);
    }

    @Test
    void factoryMethodHandsOutAProxyInsteadOfTheRealPayment() {
        Payment cash = new CashPaymentCreator().getPayment();

        assertTrue(cash instanceof PaymentProxy);
        assertFalse(cash.getClass().getSimpleName().startsWith("Cash"));
        assertThrows(PaymentDeclinedException.class, () -> cash.pay(300.01)); // cash limit
    }
}
