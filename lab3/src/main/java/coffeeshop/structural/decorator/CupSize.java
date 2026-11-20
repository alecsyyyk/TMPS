package coffeeshop.structural.decorator;

public enum CupSize {
    SMALL(0.0), MEDIUM(5.0), LARGE(10.0);

    private final double extraPrice;

    CupSize(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public double getExtraPrice() {
        return extraPrice;
    }
}
