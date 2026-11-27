package coffeeshop;

import coffeeshop.creational.factory.MorningComboCreator;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.domain.Cappuccino;
import coffeeshop.domain.Croissant;
import coffeeshop.domain.Espresso;
import coffeeshop.domain.Latte;
import coffeeshop.domain.Product;
import coffeeshop.structural.composite.ProductBundle;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CompositeTest {
    private static final double DELTA = 1e-9;

    // The same helper works for a leaf and for a bundle: no instanceof.
    private static int countLeaves(Product product) {
        if (product.getChildren().isEmpty()) {
            return 1;
        }
        int count = 0;
        for (Product child : product.getChildren()) {
            count += countLeaves(child);
        }
        return count;
    }

    @Test
    void leafAndBundleAreUsedThroughTheSameInterface() {
        Product leaf = new Latte();
        Product bundle = new ProductBundle("Duo", 0).add(new Espresso()).add(new Croissant());

        List<Product> items = List.of(leaf, bundle);
        double total = 0;
        for (Product item : items) {
            total += item.getPrice();
        }

        assertEquals(40.0 + 25.0 + 30.0, total, DELTA);
        assertTrue(leaf.getChildren().isEmpty());
        assertEquals(2, bundle.getChildren().size());
    }

    @Test
    void nestedBundlesComputePriceRecursively() {
        Product morningCombo = new MorningComboCreator().createProduct();   // (25 + 30) * 0.9 = 49.5
        Product officePack = new OfficePackCreator().createProduct();       // (40 + 50 + 38 + 49.5) * 0.8

        assertEquals(49.5, morningCombo.getPrice(), DELTA);
        assertEquals(142.0, officePack.getPrice(), DELTA);
        assertEquals(5, countLeaves(officePack)); // Latte, large Latte, Cappuccino, Espresso, Croissant
    }

    @Test
    void bundleDiscountAppliesToAllChildren() {
        ProductBundle bundle = new ProductBundle("Trio", 25)
                .add(new Latte())
                .add(new Cappuccino())
                .add(new Espresso());

        assertEquals((40.0 + 38.0 + 25.0) * 0.75, bundle.getPrice(), DELTA);
        assertEquals("Trio (-25%)", bundle.getName());
    }

    @Test
    void bundleCannotContainItself() {
        ProductBundle outer = new ProductBundle("Outer", 0);
        ProductBundle inner = new ProductBundle("Inner", 0).add(outer);

        assertThrows(IllegalArgumentException.class, () -> outer.add(outer));
        assertThrows(IllegalArgumentException.class, () -> outer.add(inner));
    }
}
