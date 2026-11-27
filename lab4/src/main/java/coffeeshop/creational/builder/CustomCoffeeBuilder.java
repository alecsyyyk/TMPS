package coffeeshop.creational.builder;

import coffeeshop.domain.Coffee;
import coffeeshop.structural.decorator.CupSize;
import coffeeshop.structural.decorator.ExtraShotDecorator;
import coffeeshop.structural.decorator.MilkDecorator;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.decorator.SizeDecorator;
import coffeeshop.structural.decorator.SyrupDecorator;
import coffeeshop.structural.decorator.WhippedCreamDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.UnaryOperator;

// Builder (Lab 2): a custom coffee is described step by step.
// Lab 3: build() no longer creates one big CustomCoffee class; it wraps the
// base coffee in a chain of decorators, in the order the steps were given.
public class CustomCoffeeBuilder {
    private final Coffee base;
    private CupSize size = CupSize.SMALL;
    private final List<UnaryOperator<Coffee>> extras = new ArrayList<>();

    public CustomCoffeeBuilder(Coffee base) {
        this.base = Objects.requireNonNull(base, "A custom coffee needs a base coffee.");
    }

    public CustomCoffeeBuilder size(CupSize size) {
        this.size = Objects.requireNonNull(size);
        return this;
    }

    public CustomCoffeeBuilder milk(MilkType milk) {
        return with(coffee -> new MilkDecorator(coffee, milk));
    }

    public CustomCoffeeBuilder extraShot() {
        return with(ExtraShotDecorator::new);
    }

    public CustomCoffeeBuilder syrup(String flavor) {
        return with(coffee -> new SyrupDecorator(coffee, flavor));
    }

    public CustomCoffeeBuilder whippedCream() {
        return with(WhippedCreamDecorator::new);
    }

    // Any decorator, including ones written after this builder, can be added
    // without changing this class: builder.with(MyNewDecorator::new).
    public CustomCoffeeBuilder with(UnaryOperator<Coffee> decorator) {
        extras.add(Objects.requireNonNull(decorator));
        return this;
    }

    public Coffee build() {
        Coffee coffee = base;
        if (size != CupSize.SMALL) {
            coffee = new SizeDecorator(coffee, size);
        }
        for (UnaryOperator<Coffee> extra : extras) {
            coffee = extra.apply(coffee);
        }
        return coffee;
    }
}
