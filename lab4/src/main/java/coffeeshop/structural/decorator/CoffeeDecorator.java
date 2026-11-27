package coffeeshop.structural.decorator;

import coffeeshop.domain.Coffee;

import java.util.Objects;

// Decorator - base Decorator.
// It IS a Coffee (same interface) and HAS a Coffee (the wrapped object), so
// decorators can wrap a plain coffee or another decorator, in any order and
// any number of times. Each call is forwarded to the wrapped coffee, then the
// add-on's own part is added.
public abstract class CoffeeDecorator implements Coffee {
    protected final Coffee coffee;

    protected CoffeeDecorator(Coffee coffee) {
        this.coffee = Objects.requireNonNull(coffee, "A decorator needs a coffee to wrap.");
    }

    @Override
    public String getName() {
        return coffee.getName() + " + " + getExtraName();
    }

    @Override
    public double getPrice() {
        return coffee.getPrice() + getExtraPrice();
    }

    protected abstract String getExtraName();

    protected abstract double getExtraPrice();
}
