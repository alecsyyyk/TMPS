package coffeeshop.creational.factory;

import coffeeshop.domain.Coffee;
import coffeeshop.domain.Espresso;

public class EspressoCreator extends CoffeeCreator {
    @Override
    public Coffee createProduct() {
        return new Espresso();
    }
}
