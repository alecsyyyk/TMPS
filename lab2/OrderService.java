// DIP: depends on abstractions injected through the constructor.
// SRP: coordinates the order workflow.
public class OrderService {
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
