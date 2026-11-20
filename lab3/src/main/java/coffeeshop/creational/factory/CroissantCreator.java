package coffeeshop.creational.factory;

import coffeeshop.domain.Croissant;
import coffeeshop.domain.Product;

public class CroissantCreator extends ProductCreator<Product> {
    @Override
    public Product createProduct() {
        return new Croissant();
    }
}
