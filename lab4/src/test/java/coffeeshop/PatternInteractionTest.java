package coffeeshop;

import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Latte;
import coffeeshop.domain.Order;
import coffeeshop.payment.Payment;
import coffeeshop.payment.PaymentDeclinedException;
import coffeeshop.service.OrderRepository;
import coffeeshop.service.OrderService;
import coffeeshop.service.Receipt;
import coffeeshop.structural.composite.ProductBundle;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.proxy.PaymentProxy;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

// One execution path through Builder -> Decorator -> Composite -> OrderService -> Proxy.
class PatternInteractionTest {
    private static final double DELTA = 1e-9;

    private final List<Order> saved = new ArrayList<>();
    private final List<Order> printed = new ArrayList<>();
    private final OrderRepository repository = saved::add;
    private final Receipt receipt = printed::add;

    private Order teamOrder() {
        Coffee custom = new CustomCoffeeBuilder(new Latte()).milk(MilkType.OAT).extraShot().build(); // 58
        Order order = new Order();
        order.addItem(new ProductBundle("Team box", 10)
                .add(custom)
                .add(new OfficePackCreator().createProduct()));                                  // (58 + 142) * 0.9
        return order;
    }

    @Test
    void decoratedCoffeeInsideBundleIsPaidThroughProxy() {
        RecordingPayment real = new RecordingPayment();
        Payment payment = new PaymentProxy(() -> real, 1000.0);

        new OrderService(payment, repository, receipt).placeOrder(teamOrder());

        assertEquals(1, real.amounts.size());
        assertEquals(180.0, real.amounts.get(0), DELTA);
        assertEquals(1, saved.size());
        assertEquals(1, printed.size());
    }

    @Test
    void declinedPaymentStopsTheWorkflow() {
        RecordingPayment real = new RecordingPayment();
        Payment payment = new PaymentProxy(() -> real, 100.0);

        assertThrows(PaymentDeclinedException.class,
                () -> new OrderService(payment, repository, receipt).placeOrder(teamOrder()));
        assertTrue(real.amounts.isEmpty());
        assertTrue(saved.isEmpty());
        assertTrue(printed.isEmpty());
    }
}
