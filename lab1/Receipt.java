// DIP: abstraction for printing receipts.
public interface Receipt {
    void print(Order order);
}

// SRP: only responsible for printing receipts.
class ConsoleReceipt implements Receipt {
    public void print(Order order) {
        System.out.println("----- RECEIPT -----");
        for (Coffee c : order.getItems()) {
            System.out.println(c.getName() + " - " + c.getPrice());
        }
        System.out.println("TOTAL: " + order.getTotal());
        System.out.println("------------------");
    }
}