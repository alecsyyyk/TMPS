// DIP: abstraction for printing receipts.
public interface Receipt {
    void print(Order order);
}