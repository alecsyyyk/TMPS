// OCP + DIP: abstraction used by OrderService.
public interface Payment {
    void pay(double amount);
}
