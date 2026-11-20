package coffeeshop.creational.factory;

import coffeeshop.domain.Coffee;
import coffeeshop.domain.Latte;

public class LatteCreator extends CoffeeCreator {
    @Override
    public Coffee createProduct() {
        return new Latte();
    }
}
