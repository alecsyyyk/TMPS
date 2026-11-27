package coffeeshop.creational.factory;

import coffeeshop.domain.Product;

// Factory Method (Lab 2): subclasses decide which Product is created.
// Generic so a coffee creator can return Coffee and a combo creator Product.
public abstract class ProductCreator<T extends Product> {
    public abstract T createProduct();

    public T prepare() {
        T product = createProduct();
        System.out.println("Preparing " + product.getName() + "...");
        return product;
    }
}
