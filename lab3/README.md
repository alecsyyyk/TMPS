# Lab 3 - Structural Design Patterns

Coffee shop console application, continued from Lab 1 (SOLID) and Lab 2
(Factory Method, Builder, Singleton). Lab 3 adds three structural patterns:

| Pattern       | Where                                    | What it does in the app                                         |
|---------------|------------------------------------------|-----------------------------------------------------------------|
| **Decorator** | `structural/decorator`                   | Add-ons (size, milk, extra shot, syrup, whipped cream) wrap a coffee and can be stacked in any order. |
| **Composite** | `structural/composite/ProductBundle`     | Combos and packs contain products and other bundles; price and receipt are computed recursively. |
| **Proxy**     | `structural/proxy/PaymentProxy`          | Stands in for the real payment: blocks amounts over the method's limit and creates the real payment lazily. |

The full report (class diagrams, code snippets, tests, conclusions) is in
[REPORT.md](REPORT.md).

## Requirements

- JDK 17+
- Maven 3.9+

No JDK installed? Everything also runs in Docker (see below).

## Build and test

```bash
mvn test
```

## Run

```bash
# interactive coffee shop
mvn -q compile exec:java

# scripted demo that shows every pattern, then one order through all of them
mvn -q compile exec:java -Dexec.args=demo
```

### With Docker instead of a local JDK

```bash
docker run --rm -it -v "$PWD":/app -v tmps-m2:/root/.m2 -w /app \
    maven:3.9-eclipse-temurin-17 mvn test

docker run --rm -it -v "$PWD":/app -v tmps-m2:/root/.m2 -w /app \
    maven:3.9-eclipse-temurin-17 mvn -q compile exec:java -Dexec.args=demo
```

## Project structure

```text
src/main/java/coffeeshop
├── domain/                 Product, Coffee, Espresso, Latte, Cappuccino, Croissant, Order
├── payment/                Payment, CardPayment, CashPayment, PaymentDeclinedException
├── creational/
│   ├── factory/            Factory Method (Lab 2): product, combo and payment creators
│   └── builder/            Builder (Lab 2): CustomCoffeeBuilder, now builds decorator chains
├── structural/
│   ├── decorator/          CoffeeDecorator + 5 concrete decorators
│   ├── composite/          ProductBundle
│   └── proxy/              PaymentProxy
├── service/                OrderService, OrderRepository, InMemoryOrderRepository (Singleton), receipts
└── client/                 Main, ConsoleApp (interactive), Demo (scripted), Menu (composition root)

src/test/java/coffeeshop    DecoratorTest, CompositeTest, ProxyTest, PatternInteractionTest
```

## Using the app

```text
=== Place an order ===
1. Espresso - 25.00 MDL
2. Latte - 40.00 MDL
3. Cappuccino - 38.00 MDL
4. Croissant - 30.00 MDL
5. Morning combo (-10%) - 49.50 MDL
6. Office pack (-20%) - 142.00 MDL
7. Custom coffee
0. Finish order
```

- Options 1-6 add a product or a bundle to the order.
- Option 7 builds a custom coffee: pick a base, a size, then add extras one at
  a time (the same extra can be added more than once, e.g. a double shot).
- `0` finishes the order and asks for a payment method. Card accepts up to
  1000 MDL and cash up to 300 MDL. If the proxy declines the payment, you can
  pick another method.

Orders are kept in memory only.
