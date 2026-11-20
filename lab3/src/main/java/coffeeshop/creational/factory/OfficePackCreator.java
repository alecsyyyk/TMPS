package coffeeshop.creational.factory;

import coffeeshop.domain.Cappuccino;
import coffeeshop.domain.Latte;
import coffeeshop.domain.Product;
import coffeeshop.structural.composite.ProductBundle;
import coffeeshop.structural.decorator.CupSize;
import coffeeshop.structural.decorator.SizeDecorator;

// Factory Method + Composite + Decorator: a bundle that contains a decorated
// coffee and another bundle (nested composite).
public class OfficePackCreator extends ProductCreator<Product> {
    @Override
    public Product createProduct() {
        return new ProductBundle("Office pack", 20)
                .add(new Latte())
                .add(new SizeDecorator(new Latte(), CupSize.LARGE))
                .add(new Cappuccino())
                .add(new MorningComboCreator().createProduct());
    }
}
