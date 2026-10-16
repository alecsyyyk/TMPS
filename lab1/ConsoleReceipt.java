// SRP: only responsible for printing receipts.
public class ConsoleReceipt implements Receipt {
    @Override
    public void print(Order order) {
        System.out.println("----- RECEIPT -----");
        for (Coffee coffee : order.getItems()) {
            System.out.println(coffee.getName() + " - " + coffee.getPrice());
        }
        System.out.println("TOTAL: " + order.getTotal());
        System.out.println("------------------");
    }
}
