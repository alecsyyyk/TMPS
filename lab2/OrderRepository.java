// DIP: abstraction for saving orders.
public interface OrderRepository {
    void save(Order order);
}
