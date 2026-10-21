import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Singleton: the whole application shares one order storage.
// SRP: only responsible for storing orders.
public class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    private InMemoryOrderRepository() {
    }

    // Holder idiom: lazy and thread-safe, the instance is created on first use.
    private static class Holder {
        private static final InMemoryOrderRepository INSTANCE = new InMemoryOrderRepository();
    }

    public static InMemoryOrderRepository getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void save(Order order) {
        orders.add(order);
        System.out.println("Order saved. Total orders: " + orders.size());
    }

    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }
}
