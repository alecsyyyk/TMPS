package coffeeshop.client;

import coffeeshop.creational.factory.CappuccinoCreator;
import coffeeshop.creational.factory.CardPaymentCreator;
import coffeeshop.creational.factory.CashPaymentCreator;
import coffeeshop.creational.factory.CoffeeCreator;
import coffeeshop.creational.factory.CroissantCreator;
import coffeeshop.creational.factory.EspressoCreator;
import coffeeshop.creational.factory.LatteCreator;
import coffeeshop.creational.factory.MorningComboCreator;
import coffeeshop.creational.factory.OfficePackCreator;
import coffeeshop.creational.factory.PaymentCreator;
import coffeeshop.creational.factory.ProductCreator;
import coffeeshop.domain.Coffee;
import coffeeshop.structural.decorator.ExtraShotDecorator;
import coffeeshop.structural.decorator.MilkDecorator;
import coffeeshop.structural.decorator.MilkType;
import coffeeshop.structural.decorator.SyrupDecorator;
import coffeeshop.structural.decorator.WhippedCreamDecorator;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

// Composition root of the console app: the only place that lists the
// concrete creators and decorators. A new product, add-on or payment method
// is a new class plus one line here; the rest of the code is unchanged.
final class Menu {
    record Extra(String label, UnaryOperator<Coffee> decorate) {
    }

    static final Map<String, ProductCreator<?>> PRODUCTS = new LinkedHashMap<>();
    static final Map<String, CoffeeCreator> COFFEE_BASES = new LinkedHashMap<>();
    static final Map<String, Extra> EXTRAS = new LinkedHashMap<>();
    static final Map<String, PaymentCreator> PAYMENTS = new LinkedHashMap<>();

    static {
        COFFEE_BASES.put("1", new EspressoCreator());
        COFFEE_BASES.put("2", new LatteCreator());
        COFFEE_BASES.put("3", new CappuccinoCreator());

        PRODUCTS.putAll(COFFEE_BASES);
        PRODUCTS.put("4", new CroissantCreator());
        PRODUCTS.put("5", new MorningComboCreator());
        PRODUCTS.put("6", new OfficePackCreator());

        EXTRAS.put("1", new Extra("Regular milk", c -> new MilkDecorator(c, MilkType.REGULAR)));
        EXTRAS.put("2", new Extra("Oat milk", c -> new MilkDecorator(c, MilkType.OAT)));
        EXTRAS.put("3", new Extra("Extra shot", ExtraShotDecorator::new));
        EXTRAS.put("4", new Extra("Vanilla syrup", c -> new SyrupDecorator(c, "vanilla")));
        EXTRAS.put("5", new Extra("Caramel syrup", c -> new SyrupDecorator(c, "caramel")));
        EXTRAS.put("6", new Extra("Whipped cream", WhippedCreamDecorator::new));

        PAYMENTS.put("1", new CardPaymentCreator());
        PAYMENTS.put("2", new CashPaymentCreator());
    }

    private Menu() {
    }
}
