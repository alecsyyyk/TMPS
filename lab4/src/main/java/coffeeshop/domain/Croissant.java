package coffeeshop.domain;

// Composite - Leaf. Not a Coffee, so coffee add-ons cannot be applied to it.
public class Croissant implements Product {
    @Override
    public String getName() {
        return "Croissant";
    }

    @Override
    public double getPrice() {
        return 30.0;
    }
}
