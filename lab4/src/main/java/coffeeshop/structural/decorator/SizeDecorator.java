package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

// Decorator - ConcreteDecorator.
public class SizeDecorator extends CoffeeDecorator {
    private final CupSize size;

    public SizeDecorator(Coffee coffee, CupSize size) {
        super(coffee);
        this.size = size;
    }

    @Override
    protected String getExtraName() {
        return size.name().toLowerCase();
    }

    @Override
    protected double getExtraPrice() {
        return size.getExtraPrice();
    }
}
