// OCP + DIP: abstraction used by OrderService.
// New payment methods are new classes that implement Payment.
public interface Payment {
    void pay(double amount);
}
 
class CardPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Card payment: " + amount + " MDL");
    }
}
 
class CashPayment implements Payment {
    public void pay(double amount) {
        System.out.println("Cash payment: " + amount + " MDL");
    }
}
 
