package coffeeshop.creational.factory;

import coffeeshop.payment.Payment;
import coffeeshop.structural.proxy.PaymentProxy;

// Factory Method (Lab 2): subclasses decide which Payment is created.
// Lab 3: the client never receives the real payment. getPayment() returns a
// PaymentProxy that creates the real one lazily via the factory method.
public abstract class PaymentCreator {
    public abstract String getName();

    protected abstract Payment createPayment();

    // Highest amount accepted in one payment with this method.
    protected abstract double getLimit();

    public Payment getPayment() {
        return new PaymentProxy(this::createPayment, getLimit());
    }
}
