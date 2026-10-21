import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    // Factory Method: Main no longer calls new Espresso(), new Latte(), ...
    private static final Map<String, CoffeeCreator> COFFEE_MENU = new LinkedHashMap<>();
    private static final Map<String, PaymentCreator> PAYMENT_MENU = new LinkedHashMap<>();

    static {
        COFFEE_MENU.put("1", new EspressoCreator());
        COFFEE_MENU.put("2", new LatteCreator());
        COFFEE_MENU.put("3", new CappuccinoCreator());

        PAYMENT_MENU.put("1", new CardPaymentCreator());
        PAYMENT_MENU.put("2", new CashPaymentCreator());
    }

    public static void main(String[] args) {
        // Singleton: every order is stored in the same repository instance.
        OrderRepository repo = InMemoryOrderRepository.getInstance();
        Receipt receipt = new ConsoleReceipt();
        Scanner scanner = new Scanner(System.in);

        do {
            Order order = takeOrder(scanner);
            if (order.getItems().isEmpty()) {
                System.out.println("An order cannot be placed without products.");
                continue;
            }

            Payment payment = choosePayment(scanner);
            new OrderService(payment, repo, receipt).placeOrder(order);
        } while (askYesNo(scanner, "Place another order? (y/n): "));

        System.out.println("Orders placed today: "
                + InMemoryOrderRepository.getInstance().getOrders().size());
    }

    private static Order takeOrder(Scanner scanner) {
        Order order = new Order();

        System.out.println("=== Place an order ===");
        System.out.println("Choose your products:");
        printCoffeeMenu();
        System.out.println("4. Custom coffee");
        System.out.println("0. Finish order");

        while (true) {
            System.out.print("Option: ");
            String option = scanner.nextLine().trim();

            if ("0".equals(option)) {
                return order;
            }

            if ("4".equals(option)) {
                order.addItem(buildCustomCoffee(scanner));
            } else if (COFFEE_MENU.containsKey(option)) {
                order.addItem(COFFEE_MENU.get(option).prepareCoffee());
            } else {
                System.out.println("Invalid option. Choose 1, 2, 3, 4, or 0.");
                continue;
            }

            System.out.println("Product added. Add more products or choose 0 to finish.");
        }
    }

    private static void printCoffeeMenu() {
        for (Map.Entry<String, CoffeeCreator> entry : COFFEE_MENU.entrySet()) {
            Coffee coffee = entry.getValue().createCoffee();
            System.out.println(entry.getKey() + ". " + coffee.getName()
                    + " - " + coffee.getPrice() + " MDL");
        }
    }

    // Builder: optional settings are added step by step.
    private static Coffee buildCustomCoffee(Scanner scanner) {
        System.out.println("Choose the base coffee:");
        printCoffeeMenu();
        CoffeeCreator creator = chooseFrom(scanner, COFFEE_MENU);

        CustomCoffee.Builder builder = new CustomCoffee.Builder(creator.prepareCoffee());

        System.out.println("Size: 1. Small (+0)  2. Medium (+5)  3. Large (+10)");
        builder.size(chooseEnum(scanner, CustomCoffee.Size.values()));

        System.out.println("Milk: 1. None (+0)  2. Regular (+3)  3. Oat (+8)");
        builder.milk(chooseEnum(scanner, CustomCoffee.Milk.values()));

        if (askYesNo(scanner, "Extra shot (+10)? (y/n): ")) {
            builder.extraShot();
        }

        System.out.print("Syrup (+5), leave empty for none: ");
        builder.syrup(scanner.nextLine());

        CustomCoffee coffee = builder.build();
        System.out.println("Built: " + coffee.getName() + " - " + coffee.getPrice() + " MDL");
        return coffee;
    }

    private static Payment choosePayment(Scanner scanner) {
        System.out.println("Choose a payment method:");
        System.out.println("1. Card");
        System.out.println("2. Cash");
        return chooseFrom(scanner, PAYMENT_MENU).getPayment();
    }

    private static <T> T chooseFrom(Scanner scanner, Map<String, T> options) {
        while (true) {
            System.out.print("Option: ");
            T choice = options.get(scanner.nextLine().trim());
            if (choice != null) {
                return choice;
            }
            System.out.println("Invalid option. Choose one of " + options.keySet() + ".");
        }
    }

    private static <T> T chooseEnum(Scanner scanner, T[] values) {
        while (true) {
            System.out.print("Option: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (index >= 0 && index < values.length) {
                    return values[index];
                }
            } catch (NumberFormatException ignored) {
            }
            System.out.println("Invalid option. Choose 1-" + values.length + ".");
        }
    }

    private static boolean askYesNo(Scanner scanner, String question) {
        while (true) {
            System.out.print(question);
            String answer = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(answer)) return true;
            if ("n".equals(answer)) return false;
            System.out.println("Please answer y or n.");
        }
    }
}
