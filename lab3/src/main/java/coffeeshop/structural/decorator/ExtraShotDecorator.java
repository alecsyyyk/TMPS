package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

// Decorator - ConcreteDecorator. Wrap twice for a double shot.
public class ExtraShotDecorator extends CoffeeDecorator {
    private static final double PRICE = 10.0;

    public ExtraShotDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    protected String getExtraName() {
        return "extra shot";
    }

    @Override
    protected double getExtraPrice() {
        return PRICE;
    }
}
