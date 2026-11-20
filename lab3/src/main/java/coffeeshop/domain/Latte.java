package coffeeshop.domain;

// Decorator - ConcreteComponent, Composite - Leaf.
public class Latte implements Coffee {
    @Override
    public String getName() {
        return "Latte";
    }

    @Override
    public double getPrice() {
        return 40.0;
    }
}
