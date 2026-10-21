public class CardPaymentCreator extends PaymentCreator {
    @Override
    protected Payment createPayment() {
        return new CardPayment();
    }
}
