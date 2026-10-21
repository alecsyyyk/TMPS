// Factory Method: the creator declares the factory method, subclasses decide
// which concrete Coffee is created.
public abstract class CoffeeCreator {
    protected abstract Coffee createCoffee();

    // Client code works only with the Coffee abstraction.
    public Coffee prepareCoffee() {
        Coffee coffee = createCoffee();
        System.out.println("Preparing " + coffee.getName() + "...");
        return coffee;
    }
}
