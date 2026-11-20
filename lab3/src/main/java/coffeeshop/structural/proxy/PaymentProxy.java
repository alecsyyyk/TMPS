package coffeeshop.structural.proxy;

import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;

import java.util.Objects;
import java.util.function.Supplier;

// Proxy - Proxy. Same Payment interface as the real payment.
// Protection proxy: blocks amounts that are not positive or above the limit
// of the payment method, before the real payment is touched.
// Virtual proxy: the real payment (card terminal, cash drawer) is created
// only when the first payment is authorized, and then reused.
public class PaymentProxy implements Payment {
    private final Supplier<? extends Payment> realPaymentFactory;
    private final double limit;
    private Payment realPayment;

    public PaymentProxy(Supplier<? extends Payment> realPaymentFactory, double limit) {
        this.realPaymentFactory = Objects.requireNonNull(realPaymentFactory);
        if (limit <= 0) {
            throw new IllegalArgumentException("Limit must be positive.");
        }
        this.limit = limit;
    }

    @Override
    public void pay(double amount) {
        if (amount <= 0) {
            throw new PaymentDeclinedException("Amount must be positive, got " + amount + " MDL.");
        }
        if (amount > limit) {
            throw new PaymentDeclinedException(String.format(
                    "%.2f MDL is above the %.2f MDL limit for this payment method.", amount, limit));
        }
        if (realPayment == null) {
            realPayment = realPaymentFactory.get();
        }
        System.out.printf("[PaymentProxy] Authorized %.2f MDL (limit %.2f MDL).%n", amount, limit);
        realPayment.pay(amount);
    }
}
