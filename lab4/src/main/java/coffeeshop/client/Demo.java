package coffeeshop.client;

import coffeeshop.behavioral.command.AddItemCommand;
import coffeeshop.behavioral.command.CommandHistory;
import coffeeshop.behavioral.command.PayOrderCommand;
import coffeeshop.behavioral.command.RemoveItemCommand;
import coffeeshop.behavioral.observer.CustomerNotifier;
import coffeeshop.behavioral.observer.KitchenDisplay;
import coffeeshop.behavioral.observer.OrderObserver;
import coffeeshop.behavioral.observer.StatusHistoryLog;
import coffeeshop.behavioral.state.OrderTicket;
import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.CardPaymentCreator;
import coffeeshop.creational.factory.CashPaymentCreator;
import coffeeshop.creational.factory.CroissantCreator;
import coffeeshop.creational.factory.LatteCreator;
import coffeeshop.creational.factory.MorningComboCreator;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Espresso;
import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.service.ConsoleReceipt;
import coffeeshop.service.InMemoryOrderRepository;
import coffeeshop.structural.composite.ProductBundle;
import coffeeshop.structural.decorator.CupSize;
import coffeeshop.structural.decorator.ExtraShotDecorator;
import coffeeshop.structural.decorator.MilkDecorator;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.decorator.SyrupDecorator;

import java.util.List;

// Non-interactive demo: shows each pattern on its own (Lab 3 structural and
// Lab 4 behavioral), then one order that goes through all of them.
class Demo {
    // Runs every section of the demo, one after another.
    void run() {
        decorator();
        composite();
        proxy();
        state();
        observer();
        command();
        allTogether();
    }

    // DECORATOR: add-ons wrap a coffee and add their price.
    private void decorator() {
        title("1. DECORATOR - add-ons wrap a coffee at runtime");
        Coffee latte = new LatteCreator().createProduct();
        show(latte);

        Coffee a = new SyrupDecorator(new ExtraShotDecorator(new MilkDecorator(latte, MilkType.OAT)), "vanilla");
        Coffee b = new MilkDecorator(new SyrupDecorator(new ExtraShotDecorator(latte), "vanilla"), MilkType.OAT);
        show(a);
        show(b);
        System.out.println("-> Same add-ons in a different order give the same price.");

        Coffee doubleShot = new ExtraShotDecorator(new ExtraShotDecorator(latte));
        show(doubleShot);
        System.out.println("-> The same decorator can be stacked twice.");
    }

    // COMPOSITE: bundles contain products and other bundles.
    private void composite() {
        title("2. COMPOSITE - a bundle is a Product made of Products");
        Product combo = new MorningComboCreator().createProduct();
        Product pack = new OfficePackCreator().createProduct();
        printTree(combo, 0);
        printTree(pack, 0);

        double total = 0;
        for (Product product : List.of(new LatteCreator().createProduct(), combo, pack)) {
            total += product.getPrice(); // leaf and composites, same call
        }
        System.out.printf("-> Latte + Morning combo + Office pack = %.2f MDL, summed through Product only.%n", total);
    }

    // PROXY: the payment proxy checks the limit before the real payment.
    private void proxy() {
        title("3. PROXY - payment limits and lazy creation of the real payment");
        Payment cash = new CashPaymentCreator().getPayment();
        System.out.println("Got a cash Payment. The cash drawer is not opened yet.");
        tryToPay(cash, 450.0);
        System.out.println("-> Blocked by the proxy; the real CashPayment was never created.");
        tryToPay(cash, 120.0);
        tryToPay(cash, 80.0);
        System.out.println("-> Authorized calls reach the real payment; it was created only once.");
    }

    // STATE: the same action gives a different result depending on the state.
    private void state() {
        title("4. STATE - the order status decides which actions are allowed");
        OrderTicket ticket = new OrderTicket();
        ticket.addItem(new LatteCreator().createProduct());
        System.out.println("Order #" + ticket.getId() + " is " + ticket.getState().getName());

        ticket.pay(new CardPaymentCreator().getPayment());
        System.out.println("After pay(): " + ticket.getState().getName());

        tryAction("add a product while Paid", () -> ticket.addItem(new Espresso()));
        tryAction("pick up while Paid", ticket::pickUp);

        ticket.startPreparing();
        tryAction("cancel while Preparing", ticket::cancel);
        ticket.markReady();
        ticket.pickUp();
        System.out.println("Final state: " + ticket.getState().getName()
                + " (final = " + ticket.getState().isFinal() + ")");
    }

    // OBSERVER: observers are added and removed while the program runs.
    private void observer() {
        title("5. OBSERVER - kitchen screen and customer SMS follow the order");
        OrderTicket ticket = new OrderTicket();
        OrderObserver kitchen = new KitchenDisplay();
        OrderObserver sms = new CustomerNotifier("Ana");
        ticket.subscribe(kitchen);
        ticket.subscribe(sms);

        ticket.addItem(new Espresso());
        ticket.pay(new CashPaymentCreator().getPayment());
        ticket.startPreparing();

        System.out.println("-> Ana turns off SMS notifications.");
        ticket.unsubscribe(sms);
        ticket.markReady();
        ticket.pickUp();
        System.out.println("-> After unsubscribe only the kitchen screen got the last two changes.");
    }

    // COMMAND: actions are objects, kept in a history and undone in reverse order.
    private void command() {
        title("6. COMMAND - every change of the order can be undone");
        OrderTicket ticket = new OrderTicket();
        CommandHistory history = new CommandHistory();
        Product latte = new LatteCreator().createProduct();
        Product croissant = new CroissantCreator().createProduct();

        history.run(new AddItemCommand(ticket, latte));
        history.run(new AddItemCommand(ticket, croissant));
        history.run(new RemoveItemCommand(ticket, latte));
        printTotal(ticket);

        history.undoLast(); // the latte comes back
        printTotal(ticket);
        history.undoLast(); // the croissant goes away
        printTotal(ticket);
        System.out.println("History now: " + history.getDescriptions());
    }

    // ALL TOGETHER: one path through Lab 2, Lab 3 and Lab 4 patterns.
    // Command -> State -> (Proxy) -> State change -> Observers.
    private void allTogether() {
        title("7. ALL TOGETHER - Command -> State -> Proxy -> Observer (+ Builder, Decorator, Composite)");
        OrderTicket ticket = new OrderTicket();
        StatusHistoryLog log = new StatusHistoryLog();
        ticket.subscribe(new KitchenDisplay());
        ticket.subscribe(new CustomerNotifier("Ion"));
        ticket.subscribe(log);
        CommandHistory history = new CommandHistory();

        // Builder + Decorator (Lab 2/3)
        Coffee custom = new CustomCoffeeBuilder(new LatteCreator().prepare())
                .size(CupSize.LARGE)
                .milk(MilkType.OAT)
                .extraShot()
                .syrup("vanilla")
                .build();
        // Composite + Factory Method (Lab 2/3)
        Product teamBox = new ProductBundle("Team box", 5)
                .add(custom)
                .add(new OfficePackCreator().prepare());

        history.run(new AddItemCommand(ticket, teamBox));
        history.run(new AddItemCommand(ticket, new MorningComboCreator().prepare()));
        history.run(new AddItemCommand(ticket, new OfficePackCreator().prepare()));
        printTotal(ticket);

        // The total is above the 300 MDL cash limit: the cash PaymentProxy
        // declines it, so the state stays New and no observer is notified.
        tryAction("pay cash", () -> history.run(new PayOrderCommand(ticket, new CashPaymentCreator().getPayment())));
        System.out.println("State is still: " + ticket.getState().getName());

        // Pay by card, then undo = refund, then pay again.
        history.run(new PayOrderCommand(ticket, new CardPaymentCreator().getPayment()));
        history.undoLast();
        history.run(new PayOrderCommand(ticket, new CardPaymentCreator().getPayment()));

        ticket.startPreparing();
        tryAction("undo the payment while Preparing", history::undoLast);
        ticket.markReady();
        ticket.pickUp();

        InMemoryOrderRepository.getInstance().save(ticket.getOrder());
        new ConsoleReceipt().print(ticket.getOrder());
        System.out.println("Status history: " + log.getEntries());
    }

    // Runs an action and prints the error if the state does not allow it.
    private void tryAction(String text, Runnable action) {
        System.out.print("Try to " + text + ": ");
        try {
            action.run();
            System.out.println("OK");
        } catch (RuntimeException e) {
            System.out.println("REJECTED - " + e.getMessage());
        }
    }

    // Prints the number of items and the total of the ticket.
    private void printTotal(OrderTicket ticket) {
        System.out.printf("Order #%d: %d item(s), total %.2f MDL%n",
                ticket.getId(), ticket.getOrder().getItems().size(), ticket.getOrder().getTotal());
    }

    // Tries to pay and prints DECLINED if the proxy refuses.
    private void tryToPay(Payment payment, double amount) {
        System.out.printf("pay(%.2f): ", amount);
        try {
            payment.pay(amount);
        } catch (PaymentDeclinedException e) {
            System.out.println("DECLINED - " + e.getMessage());
        }
    }

    // Prints a product and, indented, all products inside it.
    private void printTree(Product product, int depth) {
        System.out.printf("%s%s - %.2f MDL%n", "    ".repeat(depth), product.getName(), product.getPrice());
        for (Product child : product.getChildren()) {
            printTree(child, depth + 1);
        }
    }

    // Prints one product with its price on one line.
    private void show(Product product) {
        System.out.printf("%-55s %7.2f MDL%n", product.getName(), product.getPrice());
    }

    // Prints the title of a demo section.
    private void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
