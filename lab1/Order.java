import java.util.ArrayList;
import java.util.List;

// SRP: Order stores items and calculates the total.
public class Order {
    private final List<Coffee> items = new ArrayList<>();

    public void addItem(Coffee coffee) { items.add(coffee); }
    public List<Coffee> getItems() { return items; }

    public double getTotal() {
        double total = 0;
        for (Coffee c : items) total += c.getPrice();
        return total;
    }
}

// DIP: abstraction for saving orders.
interface OrderRepository {
    void save(Order order);
}

// SRP: only responsible for storing orders.
class InMemoryOrderRepository implements OrderRepository {
    private final List<Order> orders = new ArrayList<>();

    public void save(Order order) {
        orders.add(order);
        System.out.println("Order saved. Total orders: " + orders.size());
    }
}

// DIP: depends only on interfaces (Payment, OrderRepository, Receipt),
//      injected through the constructor.
// OCP: accepts new implementations without modification.
// SRP: only coordinates the order workflow.
class OrderService {
    private final Payment payment;
    private final OrderRepository repository;
    private final Receipt receipt;

    public OrderService(Payment payment, OrderRepository repository, Receipt receipt) {
        this.payment = payment;
        this.repository = repository;
        this.receipt = receipt;
    }

    public void placeOrder(Order order) {
        payment.pay(order.getTotal());
        repository.save(order);
        receipt.print(order);
    }
}