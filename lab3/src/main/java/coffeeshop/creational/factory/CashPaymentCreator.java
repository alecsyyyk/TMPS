package coffeeshop.creational.factory;

import coffeeshop.payment.CashPayment;
import coffeeshop.payment.Payment;

public class CashPaymentCreator extends PaymentCreator {
    @Override
    public String getName() {
        return "Cash";
    }

    @Override
    protected Payment createPayment() {
        return new CashPayment();
    }

    // The counter does not keep enough change for large cash payments.
    @Override
    protected double getLimit() {
        return 300.0;
    }
}
