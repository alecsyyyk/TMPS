import java.util.ArrayList;
import java.util.List;

// SRP: Order stores items and calculates the total.
public class Order {
    private final List<Coffee> items = new ArrayList<>();

    public void addItem(Coffee coffee) { items.add(coffee); }
    public List<Coffee> getItems() { return items; }

    public double getTotal() {
        double total = 0;
        for (Coffee c : items) total += c.getPrice();
        return total;
    }
}