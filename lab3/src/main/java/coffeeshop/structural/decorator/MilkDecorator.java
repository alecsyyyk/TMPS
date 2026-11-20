package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

// Decorator - ConcreteDecorator.
public class MilkDecorator extends CoffeeDecorator {
    private final MilkType milk;

    public MilkDecorator(Coffee coffee, MilkType milk) {
        super(coffee);
        this.milk = milk;
    }

    @Override
    protected String getExtraName() {
        return milk.name().toLowerCase() + " milk";
    }

    @Override
    protected double getExtraPrice() {
        return milk.getExtraPrice();
    }
}
