package coffeeshop.domain;

import java.util.List;

// Composite - Component: everything that can be sold. Single products (leaves)
// and bundles (composites) are used through this one interface.
public interface Product {
    String getName();

    double getPrice();

    // A leaf has no children. A bundle returns its items, so a client can walk
    // the whole tree without checking types with instanceof.
    default List<Product> getChildren() {
        return List.of();
    }
}
