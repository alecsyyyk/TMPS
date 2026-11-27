package coffeeshop.client;

import coffeeshop.behavioral.command.AddItemCommand;
import coffeeshop.behavioral.command.Command;
import coffeeshop.behavioral.command.CommandHistory;
import coffeeshop.behavioral.command.PayOrderCommand;
import coffeeshop.behavioral.command.RemoveItemCommand;
import coffeeshop.behavioral.observer.CustomerNotifier;
import coffeeshop.behavioral.observer.KitchenDisplay;
import coffeeshop.behavioral.observer.OrderObserver;
import coffeeshop.behavioral.observer.StatusHistoryLog;
import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.PaymentCreator;
import coffeeshop.creational.factory.ProductCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;
import coffeeshop.service.ConsoleReceipt;
import coffeeshop.service.InMemoryOrderRepository;
import coffeeshop.service.OrderRepository;
import coffeeshop.service.Receipt;
import coffeeshop.structural.decorator.CupSize;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Interactive console client.
// Lab 4: the user works with one OrderTicket at a time.
//  - every change of the order goes through a Command (so it can be undone),
//  - the ticket's State decides which actions are allowed,
//  - Observers (kitchen screen, customer SMS) are told about every change,
//    and the customer SMS can be turned on/off while the app runs.
// Products and payments still come from the Lab 2/3 factories, builder,
// decorators, composite bundles and payment proxy.
class ConsoleApp {
    private final Scanner scanner;
    private final OrderRepository repository = InMemoryOrderRepository.getInstance();
    private final Receipt receipt = new ConsoleReceipt();

    // Saves the scanner used to read what the user types.
    ConsoleApp(Scanner scanner) {
        this.scanner = scanner;
    }

    // Main loop: one order after another, until the user says "n".
    void run() {
        do {
            handleOneOrder();
        } while (askYesNo("Start another order? (y/n): "));

        System.out.println("Orders completed today: "
                + InMemoryOrderRepository.getInstance().getOrders().size());
    }

    // Creates a ticket, subscribes the observers and shows the menu until the
    // order is completed or cancelled.
    private void handleOneOrder() {
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();

        OrderObserver kitchen = new KitchenDisplay();
        OrderObserver sms = new CustomerNotifier("Customer");
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(kitchen);
        ticket.subscribe(sms);
        ticket.subscribe(log);

        while (!ticket.getState().isFinal()) {
            printMenu(ticket);
            System.out.print("Action: ");
            String action = scanner.nextLine().trim().toLowerCase();
            // Every action can be rejected by the current state. Then we just
            // show the message and let the user choose again.
            try {
                if (!doAction(action, ticket, history, sms)) {
                    System.out.println("Unknown action.");
                }
            } catch (RuntimeException e) {
                System.out.println("Not possible: " + e.getMessage());
            }
        }

        System.out.println("Status history: " + log.getEntries());
    }

    // Runs one menu action. Returns false if the letter is unknown.
    private boolean doAction(String action, OrderTicket ticket, CommandHistory history, OrderObserver sms) {
        switch (action) {
            case "a":
                runCommand(history, new AddItemCommand(ticket, chooseProduct()));
                return true;
            case "c":
                runCommand(history, new AddItemCommand(ticket, buildCustomCoffee()));
                return true;
            case "d":
                runCommand(history, new RemoveItemCommand(ticket, chooseItemFromOrder(ticket)));
                return true;
            case "p":
                runCommand(history, new PayOrderCommand(ticket, choosePayment()));
                return true;
            case "u":
                if (!history.undoLast()) {
                    System.out.println("Nothing to undo.");
                }
                return true;
            case "s":
                ticket.startPreparing();
                return true;
            case "r":
                ticket.markReady();
                return true;
            case "k":
                ticket.pickUp();
                // The order is finished: save it (Singleton) and print the receipt (Composite).
                repository.save(ticket.getOrder());
                receipt.print(ticket.getOrder());
                return true;
            case "x":
                ticket.cancel();
                return true;
            case "n":
                toggleSms(ticket, sms);
                return true;
            case "h":
                System.out.println("Undo history: " + history.getDescriptions());
                return true;
            default:
                return false;
        }
    }

    // Runs a command through the invoker (so it is saved for undo).
    private void runCommand(CommandHistory history, Command command) {
        history.run(command);
    }

    // Subscribes or unsubscribes the customer SMS observer at runtime.
    private void toggleSms(OrderTicket ticket, OrderObserver sms) {
        if (ticket.isSubscribed(sms)) {
            ticket.unsubscribe(sms);
            System.out.println("Customer SMS turned OFF.");
        } else {
            ticket.subscribe(sms);
            System.out.println("Customer SMS turned ON.");
        }
    }

    // Prints the current order and the list of actions.
    private void printMenu(OrderTicket ticket) {
        System.out.println();
        System.out.printf("=== Order #%d [%s] - %d item(s), total %.2f MDL ===%n",
                ticket.getId(), ticket.getState().getName(),
                ticket.getOrder().getItems().size(), ticket.getOrder().getTotal());
        System.out.println("a - add product        c - add custom coffee   d - remove product");
        System.out.println("p - pay                u - undo last action     h - show undo history");
        System.out.println("s - start preparing    r - mark ready           k - pick up");
        System.out.println("x - cancel order       n - customer SMS on/off");
    }

    // Shows the product menu and returns the chosen product (Factory Method).
    private Product chooseProduct() {
        printProducts(Menu.PRODUCTS);
        return chooseFrom(Menu.PRODUCTS).prepare();
    }

    // Shows the products already in the order and returns the chosen one.
    private Product chooseItemFromOrder(OrderTicket ticket) {
        List<Product> items = ticket.getOrder().getItems();
        if (items.isEmpty()) {
            throw new IllegalStateException("the order has no products.");
        }
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%d. %s - %.2f MDL%n", i + 1, items.get(i).getName(), items.get(i).getPrice());
        }
        return chooseEnum(items.toArray(new Product[0]));
    }

    // Shows the payment methods and returns a Payment (really a PaymentProxy).
    private Payment choosePayment() {
        System.out.println("Choose a payment method:");
        for (Map.Entry<String, PaymentCreator> entry : Menu.PAYMENTS.entrySet()) {
            System.out.println(entry.getKey() + ". " + entry.getValue().getName());
        }
        return chooseFrom(Menu.PAYMENTS).getPayment();
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

    // Prints a numbered list of products with their price.
    private void printProducts(Map<String, ? extends ProductCreator<?>> menu) {
        for (Map.Entry<String, ? extends ProductCreator<?>> entry : menu.entrySet()) {
            Product product = entry.getValue().createProduct();
            System.out.printf("%s. %s - %.2f MDL%n", entry.getKey(), product.getName(), product.getPrice());
        }
    }

    // Asks until the user types a key that exists in the map.
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

    // Asks until the user types a number between 1 and values.length.
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

    // Asks a yes/no question until the answer is "y" or "n".
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
