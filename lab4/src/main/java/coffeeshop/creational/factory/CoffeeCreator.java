package coffeeshop.creational.factory;

import coffeeshop.domain.Coffee;

// Factory Method: creators of plain coffees. Their products can be used as
// the base of a custom (decorated) coffee.
public abstract class CoffeeCreator extends ProductCreator<Coffee> {
}
