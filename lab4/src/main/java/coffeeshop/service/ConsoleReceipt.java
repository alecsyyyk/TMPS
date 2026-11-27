package coffeeshop.service;

import coffeeshop.domain.Order;
import coffeeshop.domain.Product;

// SRP: only responsible for printing receipts.
// Composite client: walks the product tree through the Product interface.
// Leaves and bundles are printed by the same code, without instanceof.
public class ConsoleReceipt implements Receipt {
    @Override
    public void print(Order order) {
        System.out.println("----- RECEIPT -----");
        for (Product product : order.getItems()) {
            printItem(product, 0);
        }
        System.out.printf("TOTAL: %.2f MDL%n", order.getTotal());
        System.out.println("-------------------");
    }

    private void printItem(Product product, int depth) {
        System.out.printf("%s%s - %.2f%n", "    ".repeat(depth), product.getName(), product.getPrice());
        for (Product child : product.getChildren()) {
            printItem(child, depth + 1);
        }
    }
}
