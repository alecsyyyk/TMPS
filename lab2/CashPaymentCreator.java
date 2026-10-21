public class CashPaymentCreator extends PaymentCreator {
    @Override
    protected Payment createPayment() {
        return new CashPayment();
    }
}
