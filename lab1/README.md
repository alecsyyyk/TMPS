# Lab 1 - Coffee Order Application

## Description

This project is a small Java console application for placing coffee orders. Different coffee products (Espresso, Latte, Cappuccino) can be added to an order, a payment method such as card or cash can be used, and a receipt is printed in the console.

The project demonstrates three SOLID design principles. In accordance with the Single Responsibility Principle (SRP), each class has a clearly defined responsibility: Order calculates the order total, InMemoryOrderRepository stores orders, ConsoleReceipt prints receipts, and OrderService coordinates the ordering process. The Open/Closed Principle (OCP) is demonstrated by allowing new coffee types and payment methods to be added through new classes without modifying the existing order-processing logic. Finally, according to the Dependency Inversion Principle (DIP), OrderService depends on abstractions such as Payment, Receipt, and OrderRepository rather than on concrete implementations, making the components easier to replace, test, and extend

## Features

- Add Espresso, Latte, or Cappuccino to an order.
- Add more than one product to the same order.
- Calculate the total price in MDL.
- Pay by card or cash.
- Save the order in memory.
- Print a receipt in the console.
- Show messages for invalid menu options.


## Project Structure

- `Main.java` - starts the application and reads user input.
- `Coffee.java` - defines the coffee interface and coffee types.
- `Order.java` - stores products and calculates the order total.
- `Payment.java` - defines card and cash payment methods.
- `Receipt.java` - defines the console receipt.
- `pom.xml` - Maven project configuration.

## How to Use

When the application starts, choose a product:

```text
1. Espresso - 25.0 MDL
2. Latte - 40.0 MDL
3. Cappuccino - 38.0 MDL
0. Finish order
```

Enter `1`, `2`, or `3` to add a product. Enter `0` when the order is ready.
After that, choose a payment method:

```text
1. Card
2. Cash
```

The application then processes the payment, saves the order, and prints the
receipt.

## Example

```text
Product added. Add more products or choose 0 to finish.
Choose a payment method:
1. Card
2. Cash
Card payment: 65.0 MDL
Order saved. Total orders: 1
----- RECEIPT -----
Espresso - 25.0
Latte - 40.0
TOTAL: 65.0
------------------
```

## Notes

Orders are saved only in memory. They are lost when the application stops.
