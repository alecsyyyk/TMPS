package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

// Decorator - ConcreteDecorator.
public class SyrupDecorator extends CoffeeDecorator {
    private static final double PRICE = 5.0;

    private final String flavor;

    public SyrupDecorator(Coffee coffee, String flavor) {
        super(coffee);
        if (flavor == null || flavor.isBlank()) {
            throw new IllegalArgumentException("Syrup flavor cannot be empty.");
        }
        this.flavor = flavor.trim();
    }

    @Override
    protected String getExtraName() {
        return flavor + " syrup";
    }

    @Override
    protected double getExtraPrice() {
        return PRICE;
    }
}
