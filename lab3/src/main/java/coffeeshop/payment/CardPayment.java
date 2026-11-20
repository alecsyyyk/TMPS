package coffeeshop.payment;

// Proxy - RealSubject. Creating it "connects" to the card terminal, which is
// the expensive step that PaymentProxy delays until a payment is authorized.
public class CardPayment implements Payment {
    public CardPayment() {
        System.out.println("Connecting to the card terminal...");
    }

    @Override
    public void pay(double amount) {
        System.out.printf("Card payment: %.2f MDL%n", amount);
    }
}
