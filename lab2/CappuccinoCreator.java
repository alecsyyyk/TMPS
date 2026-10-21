public class CappuccinoCreator extends CoffeeCreator {
    @Override
    protected Coffee createCoffee() {
        return new Cappuccino();
    }
}
