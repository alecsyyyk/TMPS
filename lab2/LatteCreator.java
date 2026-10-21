public class LatteCreator extends CoffeeCreator {
    @Override
    protected Coffee createCoffee() {
        return new Latte();
    }
}
