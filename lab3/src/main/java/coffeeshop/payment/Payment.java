package coffeeshop.payment;

// OCP + DIP: abstraction used by OrderService.
// Proxy - Subject: the real payments and PaymentProxy share this interface.
public interface Payment {
    // Throws PaymentDeclinedException when the payment is not allowed.
    void pay(double amount);
}
