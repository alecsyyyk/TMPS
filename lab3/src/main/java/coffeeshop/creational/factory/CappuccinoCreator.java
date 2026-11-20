package coffeeshop.creational.factory;

import coffeeshop.domain.Coffee;
import coffeeshop.domain.Cappuccino;

public class CappuccinoCreator extends CoffeeCreator {
    @Override
    public Coffee createProduct() {
        return new Cappuccino();
    }
}
