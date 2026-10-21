// Factory Method: subclasses decide which Payment implementation is created.
public abstract class PaymentCreator {
    protected abstract Payment createPayment();

    // Client code receives only the Payment abstraction.
    public Payment getPayment() {
        return createPayment();
    }
}
