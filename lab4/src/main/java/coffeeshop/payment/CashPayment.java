package coffeeshop.payment;

// Proxy - RealSubject.
public class CashPayment implements Payment {
    public CashPayment() {
        System.out.println("Opening the cash drawer...");
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Cash payment: %.2f MDL%n", amount);
    }
}
