package coffeeshop.structural.decorator;

public enum MilkType {
    REGULAR(3.0), OAT(8.0);

    private final double extraPrice;

    MilkType(double extraPrice) {
        this.extraPrice = extraPrice;
    }

    public double getExtraPrice() {
        return extraPrice;
    }
}
