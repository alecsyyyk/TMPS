// OCP: Coffee este punctul de extindere.
// Cafele noi = clase noi care implementeaza Coffee, fara a modifica restul codului.
public interface Coffee {
    String getName();
    double getPrice();
}

class Espresso implements Coffee {
    public String getName() { return "Espresso"; }
    public double getPrice() { return 25.0; }
}

class Latte implements Coffee {
    public String getName() { return "Latte"; }
    public double getPrice() { return 40.0; }
}

class Cappuccino implements Coffee {
    public String getName() { return "Cappuccino"; }
    public double getPrice() { return 38.0; }
}