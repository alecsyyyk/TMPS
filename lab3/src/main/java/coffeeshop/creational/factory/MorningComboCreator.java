package coffeeshop.creational.factory;

import coffeeshop.domain.Croissant;
import coffeeshop.domain.Espresso;
import coffeeshop.domain.Product;
import coffeeshop.structural.composite.ProductBundle;

// Factory Method + Composite: creates a bundle of leaves.
public class MorningComboCreator extends ProductCreator<Product> {
    @Override
    public Product createProduct() {
        return new ProductBundle("Morning combo", 10)
                .add(new Espresso())
                .add(new Croissant());
    }
}
