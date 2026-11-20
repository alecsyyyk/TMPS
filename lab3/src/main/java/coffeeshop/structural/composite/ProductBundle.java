package coffeeshop.structural.composite;

import coffeeshop.domain.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

// Composite - Composite.
// A bundle is a Product that contains other Products: single coffees,
// decorated coffees, pastries or other bundles. Its price is computed
// recursively from its children, then the bundle discount is applied.
public class ProductBundle implements Product {
    private final String name;
    private final int discountPercent;
    private final List<Product> items = new ArrayList<>();

    public ProductBundle(String name, int discountPercent) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("A bundle needs a name.");
        }
        if (discountPercent < 0 || discountPercent > 100) {
            throw new IllegalArgumentException("Discount must be between 0 and 100.");
        }
        this.name = name;
        this.discountPercent = discountPercent;
    }

    // Child management lives only on the composite (safe Composite):
    // a leaf cannot be asked to add children.
    public ProductBundle add(Product product) {
        Objects.requireNonNull(product, "Product cannot be null.");
        if (product == this || contains(product, this)) {
            throw new IllegalArgumentException("A bundle cannot contain itself.");
        }
        items.add(product);
        return this;
    }

    @Override
    public String getName() {
        return discountPercent == 0 ? name : name + " (-" + discountPercent + "%)";
    }

    @Override
    public double getPrice() {
        double sum = 0;
        for (Product item : items) {
            sum += item.getPrice(); // leaf or nested bundle, same call
        }
        return sum * (100 - discountPercent) / 100.0;
    }

    @Override
    public List<Product> getChildren() {
        return Collections.unmodifiableList(items);
    }

    private static boolean contains(Product root, Product target) {
        for (Product child : root.getChildren()) {
            if (child == target || contains(child, target)) {
                return true;
            }
        }
        return false;
    }
}
