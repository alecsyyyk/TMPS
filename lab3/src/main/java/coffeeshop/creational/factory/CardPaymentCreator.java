package coffeeshop.creational.factory;

import coffeeshop.payment.CardPayment;
import coffeeshop.payment.Payment;

public class CardPaymentCreator extends PaymentCreator {
    @Override
    public String getName() {
        return "Card";
    }

    @Override
    protected Payment createPayment() {
        return new CardPayment();
    }

    @Override
    protected double getLimit() {
        return 1000.0;
    }
}
