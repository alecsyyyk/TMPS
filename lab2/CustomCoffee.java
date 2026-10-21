// Builder: a customized coffee is built step by step from a base coffee.
// The object is immutable once build() is called.
public class CustomCoffee implements Coffee {
    public enum Size {
        SMALL(0.0), MEDIUM(5.0), LARGE(10.0);

        private final double extraPrice;

        Size(double extraPrice) { this.extraPrice = extraPrice; }
    }

    public enum Milk {
        NONE(0.0), REGULAR(3.0), OAT(8.0);

        private final double extraPrice;

        Milk(double extraPrice) { this.extraPrice = extraPrice; }
    }

    private static final double EXTRA_SHOT_PRICE = 10.0;
    private static final double SYRUP_PRICE = 5.0;

    private final Coffee base;
    private final Size size;
    private final Milk milk;
    private final boolean extraShot;
    private final String syrup;

    private CustomCoffee(Builder builder) {
        this.base = builder.base;
        this.size = builder.size;
        this.milk = builder.milk;
        this.extraShot = builder.extraShot;
        this.syrup = builder.syrup;
    }

    @Override
    public String getName() {
        StringBuilder name = new StringBuilder(base.getName())
                .append(" (").append(size.name().toLowerCase());
        if (milk != Milk.NONE) name.append(", ").append(milk.name().toLowerCase()).append(" milk");
        if (extraShot) name.append(", extra shot");
        if (syrup != null) name.append(", ").append(syrup).append(" syrup");
        return name.append(")").toString();
    }

    @Override
    public double getPrice() {
        double price = base.getPrice() + size.extraPrice + milk.extraPrice;
        if (extraShot) price += EXTRA_SHOT_PRICE;
        if (syrup != null) price += SYRUP_PRICE;
        return price;
    }

    public static class Builder {
        private final Coffee base;
        private Size size = Size.SMALL;
        private Milk milk = Milk.NONE;
        private boolean extraShot;
        private String syrup;

        public Builder(Coffee base) {
            if (base == null) {
                throw new IllegalArgumentException("A custom coffee needs a base coffee.");
            }
            this.base = base;
        }

        public Builder size(Size size) {
            this.size = size;
            return this;
        }

        public Builder milk(Milk milk) {
            this.milk = milk;
            return this;
        }

        public Builder extraShot() {
            this.extraShot = true;
            return this;
        }

        public Builder syrup(String syrup) {
            this.syrup = (syrup == null || syrup.isBlank()) ? null : syrup.trim();
            return this;
        }

        public CustomCoffee build() {
            return new CustomCoffee(this);
        }
    }
}
