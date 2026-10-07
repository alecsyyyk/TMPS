import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        OrderRepository repo = new InMemoryOrderRepository();
        Receipt receipt = new ConsoleReceipt();
        Scanner scanner = new Scanner(System.in);
        Order order = new Order();

        System.out.println("=== Place an order ===");
        System.out.println("Choose your products:");
        System.out.println("1. Espresso - 25.0 MDL");
        System.out.println("2. Latte - 40.0 MDL");
        System.out.println("3. Cappuccino - 38.0 MDL");
        System.out.println("0. Finish order");

        while (true) {
            System.out.print("Option: ");
            String option = scanner.nextLine().trim();

            if ("0".equals(option)) {
                break;
            }

            switch (option) {
                case "1" -> order.addItem(new Espresso());
                case "2" -> order.addItem(new Latte());
                case "3" -> order.addItem(new Cappuccino());
                default -> {
                    System.out.println("Invalid option. Choose 1, 2, 3, or 0.");
                    continue;
                }
            }

            System.out.println("Product added. Add more products or choose 0 to finish.");
        }

        if (order.getItems().isEmpty()) {
            System.out.println("An order cannot be placed without products.");
            return;
        }

        Payment payment = choosePayment(scanner);
        new OrderService(payment, repo, receipt).placeOrder(order);
    }

    private static Payment choosePayment(Scanner scanner) {
        while (true) {
            System.out.println("Choose a payment method:");
            System.out.println("1. Card");
            System.out.println("2. Cash");
            System.out.print("Option: ");

            switch (scanner.nextLine().trim()) {
                case "1" -> {
                    return new CardPayment();
                }
                case "2" -> {
                    return new CashPayment();
                }
                default -> System.out.println("Invalid option. Choose 1 or 2.");
            }
        }
    }
}