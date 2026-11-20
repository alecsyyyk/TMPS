package coffeeshop.client;

import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.CardPaymentCreator;
import coffeeshop.creational.factory.CashPaymentCreator;
import coffeeshop.creational.factory.LatteCreator;
import coffeeshop.creational.factory.MorningComboCreator;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Order;
import coffeeshop.domain.Product;
import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.service.ConsoleReceipt;
import coffeeshop.service.InMemoryOrderRepository;
import coffeeshop.service.OrderService;
import coffeeshop.structural.composite.ProductBundle;
import coffeeshop.structural.decorator.CupSize;
import coffeeshop.structural.decorator.ExtraShotDecorator;
import coffeeshop.structural.decorator.MilkDecorator;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.decorator.SyrupDecorator;

import java.util.List;

// Non-interactive demo: shows each pattern on its own, then one order that
// goes through all of them.
class Demo {
    void run() {
        decorator();
        composite();
        proxy();
        allTogether();
    }

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

    private void allTogether() {
        title("4. ALL TOGETHER - Factory -> Builder -> Decorator -> Composite -> Proxy -> Singleton");
        Coffee custom = new CustomCoffeeBuilder(new LatteCreator().prepare())
                .size(CupSize.LARGE)
                .milk(MilkType.OAT)
                .extraShot()
                .syrup("vanilla")
                .build();

        Product teamBox = new ProductBundle("Team box", 5)
                .add(custom)
                .add(new OfficePackCreator().prepare());

        Order order = new Order();
        order.addItem(teamBox);
        order.addItem(new MorningComboCreator().prepare());

        Payment payment = new CardPaymentCreator().getPayment();
        new OrderService(payment, InMemoryOrderRepository.getInstance(), new ConsoleReceipt())
                .placeOrder(order);
    }

    private void tryToPay(Payment payment, double amount) {
        System.out.printf("pay(%.2f): ", amount);
        try {
            payment.pay(amount);
        } catch (PaymentDeclinedException e) {
            System.out.println("DECLINED - " + e.getMessage());
        }
    }

    private void printTree(Product product, int depth) {
        System.out.printf("%s%s - %.2f MDL%n", "    ".repeat(depth), product.getName(), product.getPrice());
        for (Product child : product.getChildren()) {
            printTree(child, depth + 1);
        }
    }

    private void show(Product product) {
        System.out.printf("%-55s %7.2f MDL%n", product.getName(), product.getPrice());
    }

    private void title(String text) {
        System.out.println();
        System.out.println("=== " + text + " ===");
    }
}
