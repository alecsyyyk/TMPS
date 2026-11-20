package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

// Decorator - ConcreteDecorator. Added in Lab 3 without changing any other
// class: proof that the decorator family is open for extension.
public class WhippedCreamDecorator extends CoffeeDecorator {
    private static final double PRICE = 6.0;

    public WhippedCreamDecorator(Coffee coffee) {
        super(coffee);
    }

    @Override
    protected String getExtraName() {
        return "whipped cream";
    }

    @Override
    protected double getExtraPrice() {
        return PRICE;
    }
}
