package coffeeshop.client;

import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.PaymentCreator;
import coffeeshop.creational.factory.ProductCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Order;
import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.service.ConsoleReceipt;
import coffeeshop.service.InMemoryOrderRepository;
import coffeeshop.service.OrderRepository;
import coffeeshop.service.OrderService;
import coffeeshop.service.Receipt;
import coffeeshop.structural.decorator.CupSize;

import java.util.Map;
import java.util.Scanner;

// Interactive console client. It works only with abstractions:
// ProductCreator, Product, Coffee, PaymentCreator, Payment, OrderService.
class ConsoleApp {
    private static final String CUSTOM_COFFEE = "7";

    private final Scanner scanner;
    private final OrderRepository repository = InMemoryOrderRepository.getInstance();
    private final Receipt receipt = new ConsoleReceipt();

    ConsoleApp(Scanner scanner) {
        this.scanner = scanner;
    }

    void run() {
        do {
            Order order = takeOrder();
            if (order.isEmpty()) {
                System.out.println("An order cannot be placed without products.");
                continue;
            }
            checkout(order);
        } while (askYesNo("Place another order? (y/n): "));

        System.out.println("Orders placed today: "
                + InMemoryOrderRepository.getInstance().getOrders().size());
    }

    private Order takeOrder() {
        Order order = new Order();

        System.out.println("=== Place an order ===");
        printProducts(Menu.PRODUCTS);
        System.out.println(CUSTOM_COFFEE + ". Custom coffee");
        System.out.println("0. Finish order");

        while (true) {
            System.out.print("Option: ");
            String option = scanner.nextLine().trim();

            if ("0".equals(option)) {
                return order;
            }
            if (CUSTOM_COFFEE.equals(option)) {
                order.addItem(buildCustomCoffee());
            } else if (Menu.PRODUCTS.containsKey(option)) {
                order.addItem(Menu.PRODUCTS.get(option).prepare());
            } else {
                System.out.println("Invalid option.");
                continue;
            }
            System.out.printf("Product added. Current total: %.2f MDL. Choose 0 to finish.%n",
                    order.getTotal());
        }
    }

    // Builder + Decorator: every extra the user picks wraps the coffee once more.
    private Coffee buildCustomCoffee() {
        System.out.println("Choose the base coffee:");
        printProducts(Menu.COFFEE_BASES);
        Coffee base = chooseFrom(Menu.COFFEE_BASES).prepare();
        CustomCoffeeBuilder builder = new CustomCoffeeBuilder(base);

        System.out.println("Size: 1. Small (+0)  2. Medium (+5)  3. Large (+10)");
        builder.size(chooseEnum(CupSize.values()));

        System.out.println("Add extras one at a time (the same extra can be added again):");
        for (Map.Entry<String, Menu.Extra> entry : Menu.EXTRAS.entrySet()) {
            Menu.Extra extra = entry.getValue();
            double extraPrice = extra.decorate().apply(base).getPrice() - base.getPrice();
            System.out.printf("%s. %s (+%.2f)%n", entry.getKey(), extra.label(), extraPrice);
        }
        System.out.println("0. Done");

        while (true) {
            System.out.print("Extra: ");
            String option = scanner.nextLine().trim();
            if ("0".equals(option)) {
                break;
            }
            Menu.Extra extra = Menu.EXTRAS.get(option);
            if (extra == null) {
                System.out.println("Invalid option.");
                continue;
            }
            builder.with(extra.decorate());
            System.out.println("Added " + extra.label() + ".");
        }

        Coffee coffee = builder.build();
        System.out.printf("Built: %s - %.2f MDL%n", coffee.getName(), coffee.getPrice());
        return coffee;
    }

    // Factory Method + Proxy: the creator hands back a Payment, which is
    // really a PaymentProxy; this client cannot tell the difference.
    private void checkout(Order order) {
        while (true) {
            System.out.println("Choose a payment method:");
            for (Map.Entry<String, PaymentCreator> entry : Menu.PAYMENTS.entrySet()) {
                System.out.println(entry.getKey() + ". " + entry.getValue().getName());
            }
            Payment payment = chooseFrom(Menu.PAYMENTS).getPayment();
            try {
                new OrderService(payment, repository, receipt).placeOrder(order);
                return;
            } catch (PaymentDeclinedException e) {
                System.out.println("Payment declined: " + e.getMessage());
                if (!askYesNo("Try another payment method? (y/n): ")) {
                    System.out.println("Order cancelled.");
                    return;
                }
            }
        }
    }

    private void printProducts(Map<String, ? extends ProductCreator<?>> menu) {
        for (Map.Entry<String, ? extends ProductCreator<?>> entry : menu.entrySet()) {
            Product product = entry.getValue().createProduct();
            System.out.printf("%s. %s - %.2f MDL%n", entry.getKey(), product.getName(), product.getPrice());
        }
    }

    private <T> T chooseFrom(Map<String, T> options) {
        while (true) {
            System.out.print("Option: ");
            T choice = options.get(scanner.nextLine().trim());
            if (choice != null) {
                return choice;
            }
            System.out.println("Invalid option. Choose one of " + options.keySet() + ".");
        }
    }

    private <T> T chooseEnum(T[] values) {
        while (true) {
            System.out.print("Option: ");
            try {
                int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (index >= 0 && index < values.length) {
                    return values[index];
                }
            } catch (NumberFormatException ignored) {
                // fall through to the error message
            }
            System.out.println("Invalid option. Choose 1-" + values.length + ".");
        }
    }

    private boolean askYesNo(String question) {
        while (true) {
            System.out.print(question);
            String answer = scanner.nextLine().trim().toLowerCase();
            if ("y".equals(answer)) {
                return true;
            }
            if ("n".equals(answer)) {
                return false;
            }
            System.out.println("Please answer y or n.");
        }
    }
}
