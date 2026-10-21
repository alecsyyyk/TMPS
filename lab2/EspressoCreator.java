public class EspressoCreator extends CoffeeCreator {
    @Override
    protected Coffee createCoffee() {
        return new Espresso();
    }
}
