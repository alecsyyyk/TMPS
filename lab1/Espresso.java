public class Espresso implements Coffee {
    @Override
    public String getName() {
        return "Espresso";
    }

    @Override
    public double getPrice() {
        return 25.0;
    }
}
