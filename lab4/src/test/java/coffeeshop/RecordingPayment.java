package coffeeshop;

import coffeeshop.payment.Payment;

import java.util.ArrayList;
import java.util.List;

// Test stub: a real subject that records every amount it receives.
public class RecordingPayment implements Payment {
    public final List<Double> amounts = new ArrayList<>();

    @Override
    public void pay(double amount) {
        amounts.add(amount);
    }
}
