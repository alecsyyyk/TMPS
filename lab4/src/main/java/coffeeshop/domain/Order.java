package coffeeshop.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// SRP: Order stores items and calculates the total.
// It works only with Product, so coffees, decorated coffees and bundles are
// all handled the same way.
public class Order {
    private final List<Product> items = new ArrayList<>();

    public void addItem(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("Product cannot be null.");
        }
        items.add(product);
    }

    // Lab 4: removes one product from the order (used by remove and undo).
    // Returns true if the product was found and removed.
    public boolean removeItem(Product product) {
        return items.remove(product);
    }

    public List<Product> getItems() {
        return Collections.unmodifiableList(items);
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        double total = 0;
        for (Product product : items) {
            total += product.getPrice();
        }
        return total;
    }
}
