package coffeeshop;

import coffeeshop.creational.builder.CustomCoffeeBuilder;
import coffeeshop.domain.Coffee;
import coffeeshop.domain.Espresso;
import coffeeshop.domain.Latte;
import coffeeshop.domain.Order;
import coffeeshop.structural.decorator.CupSize;
import coffeeshop.structural.decorator.ExtraShotDecorator;
import coffeeshop.structural.decorator.MilkDecorator;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.decorator.SyrupDecorator;
import coffeeshop.structural.decorator.WhippedCreamDecorator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DecoratorTest {
    private static final double DELTA = 1e-9;

    @Test
    void decoratorAddsItsPartOnTopOfTheWrappedCoffee() {
        Coffee coffee = new MilkDecorator(new Latte(), MilkType.OAT);

        assertEquals("Latte + oat milk", coffee.getName());
        assertEquals(40.0 + 8.0, coffee.getPrice(), DELTA);
    }

    @Test
    void decoratorsStackedInAnyOrderGiveTheSamePrice() {
        Coffee a = new SyrupDecorator(new ExtraShotDecorator(new MilkDecorator(new Latte(), MilkType.OAT)), "vanilla");
        Coffee b = new MilkDecorator(new SyrupDecorator(new ExtraShotDecorator(new Latte()), "vanilla"), MilkType.OAT);
        Coffee c = new ExtraShotDecorator(new MilkDecorator(new SyrupDecorator(new Latte(), "vanilla"), MilkType.OAT));

        double expected = 40.0 + 8.0 + 10.0 + 5.0;
        assertEquals(expected, a.getPrice(), DELTA);
        assertEquals(expected, b.getPrice(), DELTA);
        assertEquals(expected, c.getPrice(), DELTA);
        for (Coffee coffee : new Coffee[] {a, b, c}) {
            assertTrue(coffee.getName().contains("oat milk"));
            assertTrue(coffee.getName().contains("extra shot"));
            assertTrue(coffee.getName().contains("vanilla syrup"));
        }
    }

    @Test
    void sameDecoratorCanBeStackedMoreThanOnce() {
        Coffee doubleShot = new ExtraShotDecorator(new ExtraShotDecorator(new Espresso()));

        assertEquals("Espresso + extra shot + extra shot", doubleShot.getName());
        assertEquals(25.0 + 10.0 + 10.0, doubleShot.getPrice(), DELTA);
    }

    @Test
    void decoratedCoffeeIsUsedByClientCodeLikeAnyCoffee() {
        Order order = new Order();
        order.addItem(new Latte());
        order.addItem(new WhippedCreamDecorator(new Latte()));

        assertEquals(40.0 + 46.0, order.getTotal(), DELTA);
    }

    @Test
    void builderAssemblesTheSameChainAsManualDecoration() {
        Coffee built = new CustomCoffeeBuilder(new Latte())
                .size(CupSize.LARGE)
                .milk(MilkType.OAT)
                .extraShot()
                .syrup("vanilla")
                .build();

        assertEquals("Latte + large + oat milk + extra shot + vanilla syrup", built.getName());
        assertEquals(73.0, built.getPrice(), DELTA); // same price as the Lab 2 CustomCoffee
    }
}
