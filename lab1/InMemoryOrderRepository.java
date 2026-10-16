import java.util.ArrayList;
import java.util.List;

// SRP: only responsible for storing orders.
public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    @Override
    public void save(Order order) {
        orders.add(order);
        System.out.println("Order saved. Total orders: " + orders.size());
    }
}
